package org.example;

import java.util.*;

public class VendingMachine {
    private final HashMap<Product, Integer> productInventory;
    private final HashMap<Coin, Integer> coinInventory;
    private final HashMap<Coin, Integer> onHoldCoins;
    private final HashMap<Product,Integer> prices;


    public  VendingMachine(){
        productInventory= new HashMap<>();
        coinInventory=new HashMap<>();
        onHoldCoins=new HashMap<>();
        prices=new HashMap<>();
    }

    public void injectCoin(Coin coin) {
        onHoldCoins.put(coin,onHoldCoins.getOrDefault(coin,0)+1);
    }

    public void translateCoinsToInventory(){
        for(Coin coin :onHoldCoins.keySet()){
            coinInventory.put(coin,coinInventory.get(coin)+onHoldCoins.get(coin));
        }
        onHoldCoins.clear();
    }

    private int translateCoinsToMontant(){
        int montant=0;
        for(Coin coin:onHoldCoins.keySet()) montant+=getValue(coin)*onHoldCoins.get(coin);
        return montant;
    }



    public HashMap<Coin,Integer> requestProduct(Product product){
         if(productInventory.get(product)<=0) throw new QuantityException("Quantity exception");
         int montant=translateCoinsToMontant();
         if(montant<prices.get(product)) throw new MontantInsuffisanteException("push more coins");
         translateCoinsToInventory();
         HashMap<Coin,Integer> numberOfEachCoinsThatMustReturned=getChange(coinInventory,montant-prices.get(product));
         if (numberOfEachCoinsThatMustReturned.isEmpty())throw  new MachineDoesentHasSuffisantCoinsException();
         productInventory.put(product,productInventory.get(product)-1);
         updateReservoirOfCoins(numberOfEachCoinsThatMustReturned);
        return numberOfEachCoinsThatMustReturned;
    }

    public  HashMap<Coin, Integer> getChange(HashMap<Coin, Integer> availableCoins, int montant) {
        HashMap<Coin, Integer> result = new HashMap<>();
        Coin[] coins=Coin.values();
        for(Coin coin : coins) result.put(coin,0);
        Arrays.sort(coins,Comparator.reverseOrder());
        getChangeHelper(availableCoins,new HashMap<>(),0,montant,result,coins);
        if(result.isEmpty()) throw  new RuntimeException("exception ");
        return result;
    }

    public  void   getChangeHelper(HashMap<Coin,Integer> availableCoins ,HashMap<Coin,Integer> result ,int index,int montant,HashMap<Coin,Integer> r, Coin[] coins){
        if (montant == 0) {
            r.putAll(result);
        }
        for (int i=index;i<coins.length;i++) {
            int coinValue = getValue(coins[i]);
            int availableCount = availableCoins.getOrDefault(coins[i], 0);

            int coinsNeeded = montant / coinValue;
            int actualCoins = Math.min(coinsNeeded, availableCount);

            if (actualCoins > 0) {
                result.put(coins[i], actualCoins);
                montant -= actualCoins * coinValue;
                getChangeHelper(availableCoins,result,i+1,montant,r,coins);
                montant+=actualCoins*coinValue;
                result.put(coins[i],0);
            }
        }
    }

    private  int getValue(Coin coin) {
        return switch (coin) {
            case FIVE -> 5;
            case One -> 1;
            case TWO -> 2;
            case TEN -> 10;
            default -> throw new IllegalArgumentException("Unknown coin type");
        };
    }

    private void updateReservoirOfCoins(HashMap<Coin,Integer> coinsThatMustBeReturned ){
        translateCoinsToInventory();
        for(Coin key : coinsThatMustBeReturned.keySet()){
            coinInventory.put(key,coinInventory.get(key)-coinsThatMustBeReturned.get(key));
        }
    }


    // getters and setters


    public HashMap<Product, Integer> getProductInventory() {
        return productInventory;
    }

    public HashMap<Coin, Integer> getCoinInventory() {
        return coinInventory;
    }

    public HashMap<Coin, Integer> getOnHoldCoins() {
        return onHoldCoins;
    }

    public HashMap<Product, Integer> getPrices() {
        return prices;
    }


}
