FROM python:3.10.1-slim
WORKDIR /usr/src/app
COPY . .
RUN pip install --upgrade pip && pip install -r ./requirements.txt
CMD ["python", "main.py"]