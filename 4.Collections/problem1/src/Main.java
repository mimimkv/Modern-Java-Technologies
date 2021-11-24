import bg.sofia.uni.fmi.mjt.investment.wallet.InvestmentWallet;
import bg.sofia.uni.fmi.mjt.investment.wallet.Wallet;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Crypto;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Gold;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Stock;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteServiceImpl;

import java.util.Collection;


public class Main {
    public static void main(String[] args) {
        Wallet wallet = new InvestmentWallet(new QuoteServiceImpl());
        wallet.deposit(110);

        try {
            wallet.buy(new Gold("1", "gold"), 1, 10.5);
            wallet.buy(new Crypto("2", "bitcoin"), 2, 15);
            wallet.buy(new Stock("3", "stock"), 3, 12);

        } catch (Exception e) {
            e.getMessage();
        }


        Collection<Acquisition> collection1 = wallet.getAllAcquisitions();
        for (Acquisition a : collection1) {
            System.out.println(a.getAsset().getName());
        }

        System.out.println();

        Collection<Acquisition> collection2 = wallet.getLastNAcquisitions(2);
        for (Acquisition a : collection2) {
            System.out.println(a.getAsset().getName());
        }

    }
}
