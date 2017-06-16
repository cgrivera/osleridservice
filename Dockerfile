FROM python:3.5

ADD . /
RUN pip install -r /requirements.txt
EXPOSE 8000
#RUN python /manage.py makemigrations oslerids
#RUN python /manage.py migrate
#CMD /bin/bash
#CMD python manage.py runserver
CMD ["python", "manage.py",  "runserver", "0.0.0.0:8000"]

