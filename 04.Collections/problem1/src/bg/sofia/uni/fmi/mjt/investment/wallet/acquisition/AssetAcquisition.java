package bg.sofia.uni.fmi.mjt.investment.wallet.acquisition;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;

import java.time.LocalDateTime;

public class AssetAcquisition implements Acquisition {
    private Asset asset;
    private final double pricePerUnit;
    private final LocalDateTime timestamp;
    private int quantityAcquired;

    public AssetAcquisition(Asset asset, double pricePerUnit, LocalDateTime timestamp, int quantityAcquired) {
        this.asset = asset;
        this.pricePerUnit = pricePerUnit;
        this.timestamp = timestamp;
        this.quantityAcquired = quantityAcquired;
    }

    @Override
    public double getPrice() {
        return pricePerUnit;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int getQuantity() {
        return quantityAcquired;
    }

    @Override
    public Asset getAsset() {
        return asset;
    }
}
