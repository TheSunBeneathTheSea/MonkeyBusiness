package monkey.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import monkey.domain.trading.StockInfo;
import monkey.domain.trading.StockInfoRepository;
import monkey.domain.trading.StockUpdateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class StockUpdateService {
    private final StockInfoRepository stockInfoRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<StockInfo> findAllStockInfoAsc() {
        return stockInfoRepository.findAll();
    }

    public Map<String, Object[]> getStockUpdateDtoMap() throws IOException {
        Map<String, Object[]> map = new HashMap<>();

        S3Object o = amazonS3.getObject(new GetObjectRequest(bucket, "price_now.json"));
        S3ObjectInputStream objectInputStream = o.getObjectContent();
        ObjectMapper mapper = new ObjectMapper();

        List<StockUpdateDto> updateDtoList = Arrays
                .asList(mapper.readValue(objectInputStream, StockUpdateDto[].class));

        for (StockUpdateDto updateDto : updateDtoList) {
            map.put(updateDto.getTicker(), new Object[]{updateDto.getCompanyName(), updateDto.getCurrentPrice()});
        }

        return map;
    }

    @Transactional
    public String updateStocks(boolean isOpening) throws IOException {
        Map<String, Object[]> updateDtoMap = getStockUpdateDtoMap();

        List<StockInfo> infoList = stockInfoRepository.findAllStockInfoAscByTicker();
        if (infoList.size() != updateDtoMap.size()) {
            if (infoList.size() == 0) {
                Set<String> keyset = updateDtoMap.keySet();
                for (String ticker : keyset) {
                    Object[] values = updateDtoMap.get(ticker);

                    String companyName = (String) values[0];
                    int currentPrice = (int) values[1];

                    StockInfo newStock = StockInfo.builder()
                            .ticker(ticker)
                            .companyName(companyName)
                            .currentPrice(currentPrice).build();
                    newStock.updateOpenPrice();

                    stockInfoRepository.save(newStock);
                }
            }else{
               throw new IOException(
                       "stockinfo size: " + infoList.size()
                               + " != updateDto size: " + updateDtoMap.size() + "does not match");
            }
        }else{
            List<StockInfo> stockInfoList = stockInfoRepository.findAll();
            Set<String> keyset = updateDtoMap.keySet();
            for (StockInfo s : stockInfoList) {
                int currentPrice = (int) updateDtoMap.get(s.getTicker())[1];

                s.updateCurrentPrice(currentPrice);
            }
        }
        if(isOpening){
            for (int i = 0; i < infoList.size(); i++) {
                StockInfo info = infoList.get(i);

                info.updateOpenPrice();
            }
            return "market opening complete";
        }

        return "stock update complete";
    }
}
