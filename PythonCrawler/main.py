import requests
from apscheduler.schedulers.background import BlockingScheduler
from bs4 import BeautifulSoup
from multiprocessing import Pool
import time
import os
import pandas as pd

def get_info(company):
        info = get_page(company[0])
        return [company[0], company[1], info[0]]

def get_page(code):
    # url = 'https://www.google.com/finance/quote/' + code + ':KRX'
    url = 'https://finance.naver.com/item/main.nhn?code=' + code
    result = requests.get(url)
    bs_obj = BeautifulSoup(result.content.decode('euc-kr', 'replace'), "lxml")
    return [bs_obj.select_one('p.no_today').text.strip().split()[0].replace(',', '')]

        
if __name__ == '__main__':
    sched = BlockingScheduler
    start = time.time()
    company_list = []
    result_list = []

    file = pd.read_excel('./data/kospi200.xlsx', usecols="A:B", dtype=str)

    for line in file.itertuples(index=False):
        company_list.append([line[0], line[1]])

    for company in company_list:
        result_list.append(get_info(company))

    df = pd.DataFrame(list(result_list), columns=['ticker', 'companyName', 'currentPrice'])
    print(df)

    path = 'C:/crawled/data'
    os.makedirs(path, exist_ok=True)

    df.to_json('C:/crawled/data/price_now.json',
               orient='records', force_ascii=False)

    print("timelapse : ", time.time() - start)