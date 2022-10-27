import requests
from apscheduler.schedulers.background import BlockingScheduler
from bs4 import BeautifulSoup
import pandas as pd
import re
import time

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
    print("crawling start at: ", time.localtime())
    start = time.time()
    sise = []
    result_list = []

    get_info(sise)

    for info in sise:
        result_list.append([info[0], info[1], info[2].replace(",", ""), info[5], info[6], info[7]])

    df = pd.DataFrame(list(result_list), columns=['ticker', 'companyName', 'currentPrice', 'volume', 'tradedVolume', 'marketCapitalization'])
    print(df)

    print("timelapse : ", time.time() - start)

if __name__ == "__main__":
    sched = BlockingScheduler({'apscheduler.timezone': 'Asia/seoul'})

    sched.add_job(crawl, trigger='cron', second='0/10', minute='*', hour='9-14', day_of_week='mon-fri', month="*")
    sched.add_job(crawl, trigger='cron', second='0/10', minute='0-30/1', hour='15', day_of_week='mon-fri', month="*")

    sched.start()