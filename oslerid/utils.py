from . import models

def build_select(tbl):
    # DP ???: Write the full query or return a parameterized query and a tuple/list of arguments?
    # DP ???: Should Cohort Users just be a Hive table name containing the set of valid OSLER IDs?
    name = tbl['table']
    cols = tbl['columns']

    query = 'SELECT {} FROM {}'.format(cols, name)

    if tbl['conditionals'] and len(tbl['conditionals']) > 0:
        conds = ['{} {} {}'.format(c['column'], c['operator'], c['value']) for c in tbl['conditionals']]
        conds = ' AND '.join(conds)
        query += ' WHERE {}'.format(conds)

    if tbl['limit']:
        query += ' LIMIT {}'.format(tbl['limit']['limit'])

    return query

def is_subset(projection, project):
    lookup = {}
    for i in range(len(project['tables'])):
        lookup[project['tables'][i]['table']] = i

    for tbl in projection['tables']:
        name = tbl['table']
        if name not in lookup:
            return False

        cols = project['tables'][lookup[name]]['columns']
        valid = [s.strip() for s in cols.split(',')]

        for col in [s.strip() for s in tbl['columns'].split(',')]:
            if col not in valid:
                return False

    return True


def current_oslerid():
    if  len(models.OslerId.objects.all()) == 0:
        state = models.OslerId.objects.create()
        state.oslerid = "1000-0000-0000"
        state.save()

    return models.OslerId.objects.first().oslerid


def oslerid_generator(requested=2):
    val = int(current_oslerid().replace("-",""))
    state = models.OslerId.objects.first()
    state.oslerid = str(val + int(requested))
    state.save()
    result = list()
    for x in range(int(requested)):
        s = str(val+x)
        s = "-".join([s[:4], s[4:8], s[8:12]])
        model = models.OslerId()
        model.oslerid = s
        result.append(model)


    return result


