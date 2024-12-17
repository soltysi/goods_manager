package com.shopify.properties;

public class Path {

    /* Sub PATHs */
    public static final String PRODUCTS =               "/products";
    public static final String ORDERS =                 "/orders";
    public static final String PAY =                    "/pay";

    /* Path variables*/
    public static final String ID_PATH_VARIABLE =      "/{id}";
    public static final String PRODUCT_ID =            "/{productId}";
    public static final String ORDER_ID =            "/{orderId}";

    /* Base PATHs */
    public static final char   SLASH =                  '/';
    public static final char   QUESTION_MARK =          '?';
    public static final char   EQUAL_SIGN =             '=';
    public static final String API_PATH_PREFIX =        SLASH + "api";

}
