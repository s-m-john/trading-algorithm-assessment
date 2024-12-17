package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    @Override
    public Action evaluate(SimpleAlgoState state) {

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);

        /********
         *
         Explanation of the Code
         Logging: The code logs the state of the order book for transparency and debugging.

         Accessing Market Data:

         Retrieves the best bid price, best ask price, and current price from the SimpleAlgoState object.

         Buy and Sell Logic:

         Buy Order: It places a child buy order if the current price is less than 98% of
         the best ask price and the maximum number of child orders has not been reached.

         Sell Order: It places a child sell order if the current price is more than 102% of
         the best bid price and the maximum number of child orders has not been reached.

         Action Returns: The algorithm returns specific actions (buy or sell child order) based on the conditions met.
         *
         ********/


        // Access market data
        double bestBidPrice = state.getBestBidPrice();
        double bestAskPrice = state.getBestAskPrice();
        double currentPrice = state.getCurrentPrice();
        int totalQuantity = 100;

        // Define threshold for buying and selling
        double buyThreshold = bestAskPrice * 0.98;
        double sellThreshold = bestBidPrice * 1.02;


        // Define child order parameters
        int childOrderQuantity = 20;
        int maxChildOrders = totalQuantity / childOrderQuantity;

        // Count hte current number of child orders
        int currentChildOrders = state.getChildOrders().size;

        // Check to see if a child order can be created
        if (currentPrice < buyThreshold) {
            logger.info("[MYALGO] Current price is below but threshold: " currentPrice);

            // Place a buy child order if the maximum hasn't been reached
            if (currentChildOrders < maxChildOrders) {
                logger.info("[MYALGO] Creating child buy order at price: " + currentPrice);
                state.addOrder(new Order(currentPrice, childOrderQuantity));
                return new Action("BUY_CHILD_ORDER", currentPrice, childOrderQuantity);
            } else{
                logger.info("[MYALGO] Maximum buy child orders reached.");
            }
        }

        // Check if a child sell order can be created
        if (currentPrice > sellThreshold) {
            logger.info("[MYALGO] Current price is above sell threshold: " + currentPrice);

            // Place a sell child order if the maximum hasn't been reached
            if (currentChildOrders < maxChildOrders) {
                logger.info("[MYALGO] Creating child sell order at price: " + currentPrice);
                state.addOrder(new Order(currentPrice, -childOrderQuantity));
                return new Action("SELL_CHILD_ORDER", currentPrice, -childOrderQuantity);
            } else {
                logger.info("[MYALGO] Maximum sell child orders reached.")
            }
        }

        return NoAction.NoAction;
    }
}


