# osleridservice
# osleridservice
cd osleridservice
docker build -t osleridservice .
docker run -d -p 8000:8000 --name oslerid osleridservice