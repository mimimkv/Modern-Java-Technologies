package bg.sofia.uni.fmi.mjt.revolut;

import bg.sofia.uni.fmi.mjt.revolut.account.Account;
import bg.sofia.uni.fmi.mjt.revolut.card.Card;
import bg.sofia.uni.fmi.mjt.revolut.card.PhysicalCard;
import bg.sofia.uni.fmi.mjt.revolut.card.VirtualOneTimeCard;

import java.time.LocalDate;

public class Revolut implements RevolutAPI {
    private static final double EUR_TO_BGN_EXCHANGE_RATE = 1.95;
    private static final String BLOCKED_TOP_LEVEL_DOMAIN = "biz";

    private Account[] accounts;
    private Card[] cards;

    public Revolut(Account[] accounts, Card[] cards) {
        this.accounts = accounts;
        this.cards = cards;
    }

    @Override
    public boolean pay(Card card, int pin, double amount, String currency) {
        if (!cardExists(card) || !card.getType().equals(PhysicalCard.TYPE)) {
            return false;
        }

        if (!isCardValid(card, pin)) {
            return false;
        }

        for (Account account : accounts) {
            if (account.getCurrency().equals(currency)) {
                if (account.getAmount() >= amount) {
                    account.setAmount(account.getAmount() - amount);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean payOnline(Card card, int pin, double amount, String currency, String shopURL) {
        if (!cardExists(card) || !isCardValid(card, pin)) {
            return false;
        }

        if (!isDomainAccepted(shopURL)) {
            return false;
        }

        for (Account account : accounts) {
            if (account.getCurrency().equals(currency)) {
                if (account.getAmount() >= amount) {
                    account.setAmount(account.getAmount() - amount);
                    if (card.getType().equals(VirtualOneTimeCard.TYPE)) {
                        card.block();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addMoney(Account account, double amount) {
        if (!accountExists(account)) {
            return false;
        }

        double currentAmount = account.getAmount();
        account.setAmount(amount + currentAmount);
        return true;
    }

    @Override
    public boolean transferMoney(Account from, Account to, double amount) {
        if (!accountExists(from) || !accountExists(to) || from.equals(to)) {
            return false;
        }

        if (from.getAmount() < amount) {
            return false;
        }

        from.setAmount(from.getAmount() - amount);

        if (from.getCurrency().equals(to.getCurrency())) {
            to.setAmount(to.getAmount() + amount);
            return true;
        }

        double convertedAmount = 0;
        convertedAmount = from.getCurrency().equals("BGN") ? (amount / EUR_TO_BGN_EXCHANGE_RATE)
                : amount * EUR_TO_BGN_EXCHANGE_RATE;
        to.setAmount(to.getAmount() + convertedAmount);
        return true;
    }

    @Override
    public double getTotalAmount() {
        double total = 0;
        for (Account account : accounts) {
            if (account.getCurrency().equals("BGN")) {
                total += account.getAmount();
            } else {
                total += EUR_TO_BGN_EXCHANGE_RATE * account.getAmount();
            }
        }
        return total;
    }

    private boolean accountExists(Account account) {
        for (int i = 0; i < accounts.length; ++i) {
            if (accounts[i].equals(account)) {
                return true;
            }
        }
        return false;
    }

    private boolean cardExists(Card card) {
        for (int i = 0; i < cards.length; ++i) {
            if (cards[i].equals(card)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCardValid(Card card, int pin) {
        LocalDate expirationDate = card.getExpirationDate();
        if (expirationDate.isBefore(LocalDate.now()) || !card.checkPin(pin) || card.isBlocked()) {
            return false;
        }
        return true;
    }

    private boolean isDomainAccepted(String shopURL) {
        String[] tokens = shopURL.split("\\.");
        String domain = tokens[tokens.length - 1];

        if (domain.equals(BLOCKED_TOP_LEVEL_DOMAIN)) {
            return false;
        }
        return true;
    }
}
