import json

from pykafka import KafkaClient

from . import utils

KAFKA_HOSTS = 'localhost:9092'
KAFKA_TOPIC = b'project'

def project_records(projection):
    # DP NOTE: Using a common database as the queue would allow for status updates from the projector service
    configs = create_configs(projection)
    send_kafka(configs)

def create_configs(projection):
    config = {
        'topic': 'oslerid-{}'.format(projection['id']),
        'address': projection['workspace']['address'],
        'database': projection['name'],
        'projections': [{
            'query': utils.build_select(tbl),
            'table': tbl['table']
        } for tbl in projection['tables']
        ]
    }

    return config

def send_kafka(config):
    client = KafkaClient(hosts=KAFKA_HOSTS)
    topic = client.topics[KAFKA_TOPIC]
    encode = lambda x: json.dumps(x).encode('utf-8')

    with topic.get_sync_producer() as p:
        p.produce(encode(config))

def projection_log(pk, reverse=True):
    topic_name = 'oslerid-{}'.format(pk).encode('utf-8')
    client = KafkaClient(hosts=KAFKA_HOSTS)
    topic = client.topics[topic_name]
    
    # DP TODO: Figure out how to correctly read only the last X number of messages from
    #          the kafka topic. Right now it is always set to the oldest available message.
    consumer = topic.get_simple_consumer(consumer_group=b'console',
                                         consumer_id=b'console',
                                         auto_commit_enable=True,
                                         auto_commit_interval_ms=1000)
    consumer.reset_offsets([(consumer.partitions[0], 0)])
    consumer.commit_offsets()

    logs = []
    while True:
        msg = consumer.consume(False)
        if msg:
            try:
                logs.append(msg.value.decode('utf-8'))
            except:
                logs.append("Unable to decode message")
        else:
            break

    if reverse:
        logs.reverse()

    return logs
