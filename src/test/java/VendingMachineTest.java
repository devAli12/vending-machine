import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class VendingMachineTest {

    VendingMachine vendingMachine;

    @BeforeEach
    void setUp(){
        vendingMachine= new VendingMachine();
        vendingMachine.getPrices().put(Product.BUENO,12);
        vendingMachine.getPrices().put(Product.WATER,5);
        vendingMachine.getPrices().put(Product.COCA,7);
        vendingMachine.getPrices().put(Product.TWIX,10);
        vendingMachine.getProductInventory().put(Product.TWIX,10);
        vendingMachine.getProductInventory().put(Product.WATER,5);
        vendingMachine.getProductInventory().put(Product.COCA,0);
        vendingMachine.getProductInventory().put(Product.BUENO,12);
        vendingMachine.getCoinInventory().put(Coin.One,7);
        vendingMachine.getCoinInventory().put(Coin.TWO,3);
        vendingMachine.getCoinInventory().put(Coin.FIVE,2);
        vendingMachine.getCoinInventory().put(Coin.TEN,1);
    }

    @Test
    void testReturnedCoinsAfterAchat(){
        vendingMachine.injectCoin(Coin.FIVE);
        vendingMachine.injectCoin(Coin.FIVE);
        HashMap<Coin,Integer> coins = vendingMachine.requestProduct(Product.WATER);
        assertEquals(0,coins.get(Coin.One));
        assertEquals(0,coins.get(Coin.TWO));
        assertEquals(1,coins.get(Coin.FIVE));
        assertEquals(0,coins.get(Coin.TEN));
    }


    @Test
    void should_return_2_coins_of_five_and_1_coin_of_One(){
        vendingMachine.getCoinInventory().put(Coin.One,2);
        vendingMachine.getCoinInventory().put(Coin.TWO,3);
        vendingMachine.getCoinInventory().put(Coin.FIVE,0);
        vendingMachine.getCoinInventory().put(Coin.TEN,0);
        vendingMachine.injectCoin(Coin.TEN);
        HashMap<Coin,Integer> coins = vendingMachine.requestProduct(Product.WATER);
        assertEquals(1,coins.get(Coin.One));
        assertEquals(2,coins.get(Coin.TWO));
        assertEquals(0,coins.get(Coin.FIVE));
        assertEquals(0,coins.get(Coin.TEN));
    }

    @Test
    void getChange_should_return_five_coins_of_type_five(){
        vendingMachine.getCoinInventory().put(Coin.One,0);
        vendingMachine.getCoinInventory().put(Coin.TWO,5);
        vendingMachine.getCoinInventory().put(Coin.FIVE,1);
        vendingMachine.getCoinInventory().put(Coin.TEN,0);
        HashMap<Coin,Integer> coins = vendingMachine.getChange(vendingMachine.getCoinInventory(),10);
        assertEquals(0,coins.get(Coin.One));
        assertEquals(5,coins.get(Coin.TWO));
        assertEquals(0,coins.get(Coin.FIVE));
        assertEquals(0,coins.get(Coin.TEN));
    }

    @Test
    void testLaunchOfExceptionQuantity(){
        vendingMachine.injectCoin(Coin.FIVE);
        assertThrows(QuantityException.class,()->vendingMachine.requestProduct(Product.COCA));
    }

    @Test
    void testLaunchOfMontantInsufisanteException(){
        vendingMachine.injectCoin(Coin.FIVE);
        assertThrows(MontantInsuffisanteException.class,()->vendingMachine.requestProduct(Product.BUENO));
    }

    @Test
    void test_state_of_coin_inventorry_after_achat(){
        vendingMachine.injectCoin(Coin.FIVE);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.requestProduct(Product.TWIX);
        HashMap<Coin,Integer> coins = vendingMachine.getCoinInventory();
        assertEquals(6,coins.get(Coin.One));
        assertEquals(6,coins.get(Coin.TWO));
        assertEquals(3,coins.get(Coin.FIVE));
        assertEquals(1,coins.get(Coin.TEN));
    }

    @Test
    void test_state_of_coin_inventory_after_injection(){
        vendingMachine.injectCoin(Coin.FIVE);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.injectCoin(Coin.TWO);
        HashMap<Coin,Integer> coins = vendingMachine.getCoinInventory();
        assertEquals(7,coins.get(Coin.One));
        assertEquals(3,coins.get(Coin.TWO));
        assertEquals(2,coins.get(Coin.FIVE));
        assertEquals(1,coins.get(Coin.TEN));
    }

    @Test
    void test_translate_coins_to_inventory(){
        vendingMachine.injectCoin(Coin.FIVE);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.injectCoin(Coin.TWO);
        vendingMachine.translateCoinsToInventory();
        HashMap<Coin,Integer> coins = vendingMachine.getCoinInventory();
        assertEquals(7,coins.get(Coin.One));
        assertEquals(6,coins.get(Coin.TWO));
        assertEquals(3,coins.get(Coin.FIVE));
        assertEquals(1,coins.get(Coin.TEN));
    }


}
