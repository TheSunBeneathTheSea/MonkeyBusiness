import requests
from apscheduler.schedulers.background import BlockingScheduler
from bs4 import BeautifulSoup
import time
import os
import pandas as pd

df = pd.DataFrame()
sise = []

def get_info():
        pages = list(range(1, 21))
        for idx in pages:
            get_page(idx)

def get_page(pageNumber):
    # url = 'https://www.google.com/finance/quote/' + code + ':KRX'
    # url = 'https://finance.naver.com/item/main.nhn?code=' + code
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
    company_dict = {}
    result_list = []

    file = pd.read_excel('./data/kospi200.xlsx', usecols="A:B", dtype=str)

    for line in file.itertuples(index=False):
        company_dict[line[1]] = line[0]

    get_info()

    for info in sise:
        result_list.append([company_dict[info[0]], info[0], info[1].replace(",", "")])

    df = pd.DataFrame(list(result_list), columns=['ticker', 'companyName', 'currentPrice'])
    print(df)

    file_path = 'C:/crawled/data/price_now.json'
    if os.path.exists(file_path):
        os.remove(file_path)
    else:
        os.makedirs(file_path, exist_ok=True)

    df.to_json(file_path,
               orient='records', force_ascii=False)
    print("timelapse : ", time.time() - start)

if __name__ == "__main__":
    # sched = BlockingScheduler()               
        
    # sched.add_job(crawl, trigger='cron', second='0', minute='0/2', hour='9-14', day_of_week='mon-fri', month="*")    
    # sched.add_job(crawl, trigger='cron', second='0', minute='0-24/2', hour='15', day_of_week='mon-fri', month="*")

    # sched.start()
    crawl()