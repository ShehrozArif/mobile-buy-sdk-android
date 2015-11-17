/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.buy.ui.products;

import com.shopify.buy.dataprovider.BuyClient;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.Shop;

import java.util.List;

import retrofit.Callback;

/**
 * The UI should use the ProductsProvider interface to load {@link Product} objects.
 * The default implementation uses a SQLite database to allow offline product browsing.
 */
public interface ProductsProvider {

    void getShop(BuyClient buyClient, Callback<Shop> callback);

    void getAllProducts(BuyClient buyClient, Callback<List<Product>> callback);

    void getProducts(String collectionId, BuyClient buyClient, Callback<List<Product>> callback);

    void getProducts(List<String> productIds, BuyClient buyClient, Callback<List<Product>> callback);

}