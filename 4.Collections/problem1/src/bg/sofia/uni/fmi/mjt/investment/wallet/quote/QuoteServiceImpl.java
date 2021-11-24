package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.AssetType;

import java.util.EnumMap;
import java.util.Map;


public class QuoteServiceImpl implements QuoteService {

    private static final double ASK_PRICE1 = 5.0;
    private static final double ASK_PRICE2 = 8.0;
    private static final double ASK_PRICE3 = 9.0;
    private static final double ASK_PRICE4 = 10.0;
    private static final double BID_PRICE1 = 9.5;
    private static final double BID_PRICE2 = 10.2;
    private static final double BID_PRICE3 = 12.0;
    private static final double BID_PRICE4 = 14.0;

    private Map<AssetType, Quote> quoteMap = new EnumMap<>(AssetType.class);

    public QuoteServiceImpl() {
        quoteMap.put(AssetType.FIAT, new Quote(ASK_PRICE1, BID_PRICE1));
        quoteMap.put(AssetType.STOCK, new Quote(ASK_PRICE2, BID_PRICE2));
        quoteMap.put(AssetType.GOLD, new Quote(ASK_PRICE3, BID_PRICE3));
        quoteMap.put(AssetType.CRYPTO, new Quote(ASK_PRICE4, BID_PRICE4));
    }

    @Override
    public Quote getQuote(Asset asset) {
        if (asset == null) {
            throw new IllegalArgumentException("Asset cannot be null");
        }

        if (quoteMap.containsKey(asset.getType())) {
            return quoteMap.get(asset.getType());
        }

        return null;
    }
}
