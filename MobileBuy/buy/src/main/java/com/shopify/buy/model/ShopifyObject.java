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

package com.shopify.buy.model;

import com.shopify.buy.dataprovider.BuyClientFactory;

/**
 * Base class for Shopify Objects
 */
public abstract class ShopifyObject {

    protected Long id;

    /**
     * @return The unique identifier of this object within the Shopify platform.
     */
    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        ShopifyObject that = (ShopifyObject) other;

        return !(id != null ? !id.equals(that.id) : that.id != null);
    }

    /**
     * @return A JSON representation of this object.
     */
    public String toJsonString() {
        return BuyClientFactory.createDefaultGson().toJson(this);
    }

}
