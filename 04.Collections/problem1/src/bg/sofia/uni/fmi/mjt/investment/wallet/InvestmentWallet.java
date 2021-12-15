package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AssetAcquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.Quote;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvestmentWallet implements Wallet {
    private static final String ASSET_CANNOT_BE_NULL_MSG = "Asset cannot be null";
    //private static final String INVALID_ARGS_MSG = "Invalid arguments";

    private double money;
    private final QuoteService quoteService;
    private List<Acquisition> acquisitions;
    private Map<Asset, Integer> assets;

    public InvestmentWallet(QuoteService quoteService) {
        this.quoteService = quoteService;
        this.acquisitions = new ArrayList<>();
        this.assets = new HashMap<>();
    }

    @Override
    public double deposit(double cash) {
        if (cash < 0) {
            throw new IllegalArgumentException("Cash cannot be negative!");
        }

        money += cash;
        return money;
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        if (cash < 0) {
            throw new IllegalArgumentException("Cash cannot be null!");
        }

        if (cash > money) {
            throw new InsufficientResourcesException("Not enough money!");
        }

        money -= cash;
        return money;
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (maxPrice < 0) {
            throw new IllegalArgumentException("Max price cannot be negative");
        }

        if (asset == null) {
            throw new IllegalArgumentException(ASSET_CANNOT_BE_NULL_MSG);
        }

        Quote quote = quoteService.getQuote(asset);
        if (quote == null) {
            throw new UnknownAssetException("There is no defined quote for this asset!");
        }

        if (maxPrice < quote.askPrice()) {
            throw new OfferPriceException("The ask price is higher than the max price of the asset!");
        }

        double price = quantity * quote.askPrice();
        if (price > money) {
            throw new InsufficientResourcesException("Not enough money for this transaction!");
        }

        money -= price;
        Acquisition acquisition = new AssetAcquisition(asset, quote.askPrice(), LocalDateTime.now(), quantity);
        acquisitions.add(acquisition);
        insertAsset(asset, quantity);

        return acquisition;
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        if (asset == null) {
            throw new IllegalArgumentException(ASSET_CANNOT_BE_NULL_MSG);
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        if (minPrice < 0) {
            throw new IllegalArgumentException("Min price cannot be negative");
        }

        if (!assets.containsKey(asset) || assets.get(asset) < quantity) {
            throw new InsufficientResourcesException("Not enough resources");
        }

        Quote quote = quoteService.getQuote(asset);
        if (quote == null) {
            throw new UnknownAssetException("No defined quote for the asset");
        }

        if (minPrice > quote.bidPrice()) {
            throw new OfferPriceException("Min price cannot be lower than bid price");
        }

        double price = quote.bidPrice() * quantity;
        money += price;
        removeAsset(asset, quantity);

        return price;
    }

    @Override
    public double getValuation() {
        double totalValuation = 0.0;

        for (Asset asset : assets.keySet()) {
            try {
                totalValuation += getValuation(asset);
            } catch (UnknownAssetException e) {
                e.printStackTrace();
            }
        }

        return totalValuation;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        if (asset == null) {
            throw new IllegalArgumentException("Asset cannot be null");
        }

        if (!assets.containsKey(asset)) {
            throw new UnknownAssetException("Asset not found in the wallet");
        }

        Quote quote = quoteService.getQuote(asset);
        if (quote == null) {
            throw new UnknownAssetException("No defined quote for this asset");
        }

        return quote.bidPrice() * assets.get(asset);
    }

    @Override
    public Asset getMostValuableAsset() {
        Asset mostValuableAsset = null;
        double highestValuation = 0;

        for (Asset asset : assets.keySet()) {
            try {
                double currentValuation = getValuation(asset);
                if (currentValuation > highestValuation) {
                    mostValuableAsset = asset;
                    highestValuation = currentValuation;
                }
            } catch (UnknownAssetException e) {
                e.printStackTrace();
            }
        }

        return mostValuableAsset;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return List.copyOf(acquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative");
        }

        if (n > acquisitions.size()) {
            n = acquisitions.size();
        }

        return Set.copyOf(acquisitions.subList(acquisitions.size() - n, acquisitions.size()));
    }


    private void removeAsset(Asset asset, int quantity) {
        assets.put(asset, assets.get(asset) - quantity);

        if (assets.get(asset) == 0) {
            assets.remove(asset);
        }
    }

    private void insertAsset(Asset asset, int quantity) {
        if (!assets.containsKey(asset)) {
            assets.put(asset, 0);
        }

        assets.put(asset, quantity + assets.get(asset));
    }
}
