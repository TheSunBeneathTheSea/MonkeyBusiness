import requests
from apscheduler.schedulers.background import BlockingScheduler
from bs4 import BeautifulSoup
import time
import os
import pandas as pd

s3 = os.environ.get('s3')
key = os.environ.get('key')
secret = os.environ.get('secret')

aws_credentials = { "key": key, "secret": secret }


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
        cols = [ele.text.strip() for ele in cols]
        sise.append([ele for ele in cols if ele])

def crawl():
    start = time.time()
    df = []
    sise = []
    company_dict = {}
    result_list = []

    read_path = s3 + '/kospi200.xlsx'
    file = pd.read_excel(read_path, usecols="A:B", dtype=str, storage_options=aws_credentials)

    for line in file.itertuples(index=False):
        company_dict[line[1]] = line[0]

    get_info(sise)

    for info in sise:
        result_list.append([company_dict[info[0]], info[0], info[1].replace(",", "")])

    df = pd.DataFrame(list(result_list), columns=['ticker', 'companyName', 'currentPrice'])
    print(df)

    write_path = s3 + '/price_now.json'
    df.to_json(write_path,
               orient='records', force_ascii=False, storage_options=aws_credentials)
    print("timelapse : ", time.time() - start)

if __name__ == "__main__":
    sched = BlockingScheduler()               
        
    sched.add_job(crawl, trigger='cron', second='0/10', minute='*', hour='9-14', day_of_week='mon-fri', month="*")    
    sched.add_job(crawl, trigger='cron', second='0/10', minute='0-30/1', hour='15', day_of_week='mon-fri', month="*")

    sched.start()