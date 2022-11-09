import requests
from apscheduler.schedulers.background import BlockingScheduler
from apscheduler.schedulers.background import BackgroundScheduler
from bs4 import BeautifulSoup
import pandas as pd
import redis
import re
from fastapi import FastAPI
import uvicorn
import time
import json


app = FastAPI()
ordering = 0
df = pd.DataFrame()
scheduler = None
@app.on_event("startup")
def boot():
    global scheduler
    scheduler= BackgroundScheduler()
    scheduler.add_job(daily_init, trigger='cron', second='55', minute='59', hour='8', day_of_week='mon-fri', month="*")
    scheduler.add_job(crawl, trigger='cron', second='0/10', minute='*', hour='9-14', day_of_week='mon-fri', month="*")
    scheduler.add_job(crawl, trigger='cron', second='0/10', minute='0-30/1', hour='15', day_of_week='mon-fri', month="*")

    # tester
    # scheduler.add_job(crawl, 'interval', seconds=10)
    scheduler.start()

@app.get("/")
def price_now():
    return json.loads(get_def())

def daily_init():
    global ordering
    ordering = 0

def get_def():
    return df.to_json(orient='records', force_ascii=False)

def get_info(sise):
    pages = list(range(1, 21))
    for idx in pages:
        get_page(idx, sise)

def get_page(pageNumber, sise):
    url = 'https://finance.naver.com/sise/entryJongmok.naver?&page=' + str(pageNumber)
    result = requests.get(url)
    bs_obj = BeautifulSoup(result.content.decode('euc-kr', 'replace'), "lxml")
    rows = bs_obj.select('table.type_1 tr')
    for row in rows[2:-2]:
        cols = row.find_all('td')
        col = [re.search(r'[0-9]{6}', cols[0].contents[0].attrs['href']).group(0)]
        col = col + [ele.text.strip() for ele in cols]
        sise.append([ele for ele in col if ele])

def crawl():
    global df
    global ordering
    print("crawling start at: ", time.localtime())
    ordering = ordering + 1
    start = time.time()
    sise = []
    result_list = []

    get_info(sise)

    for info in sise:
            result_list.append([info[0], info[1], info[2].replace(",", ""),
                                info[5].replace(",", ""), info[6].replace(",", ""), info[7].replace(",", "")])

    df = pd.DataFrame(list(result_list), columns=[u'ticker', 'companyName', 'currentPrice', 'volume', 'tradedVolume',
                                                   'marketCapitalization'])
    print(time.strftime('%Y-%m-%d-', time.localtime()) + str(ordering))
    # print(pd.read_json(df.to_json(orient='split'), orient='split', dtype={'ticker':'str'}))
    # r.set(time.strftime('%Y-%m-%d:', time.localtime())+str(ordering), df.to_json(orient='split'))
    # r.expire(time.strftime('%Y-%m-%d:', time.localtime())+str(ordering), 86400)
    # print(r.get('2022-11-03-1'))
    print("timelapse : ", time.time() - start)

if __name__ == '__main__':
    # r = redis.Redis(host='localhost', port=6379, db=0, username='default', password='password')
    uvicorn.run("main:app", host='127.0.0.1', port=8000, reload=True)


