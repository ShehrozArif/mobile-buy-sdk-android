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

package com.shopify.buy.ui.collections;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.shopify.buy.R;
import com.shopify.buy.dataprovider.BuyClientFactory;
import com.shopify.buy.dataprovider.CollectionsProvider;
import com.shopify.buy.dataprovider.DefaultCollectionsProvider;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;
import com.shopify.buy.ui.common.BaseFragment;
import com.shopify.buy.ui.common.RecyclerViewHolder;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CollectionListFragment extends BaseFragment implements RecyclerViewHolder.ClickListener<Collection> {

    private static final String TAG = CollectionListFragment.class.getSimpleName();

    CollectionListFragmentView view;

    private List<Collection> collections;
    RecyclerView recyclerView;

    private Listener listener;

    private CollectionsProvider provider = null;

    public void setProvider(CollectionsProvider provider) {
        this.provider = provider;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (provider == null) {
            provider = new DefaultCollectionsProvider(getActivity());
        }

        Bundle bundle = getArguments();

        if (bundle.containsKey(CollectionListConfig.EXTRA_SHOP_COLLECTIONS)) {
            String collectionsJson = bundle.getString(CollectionListConfig.EXTRA_SHOP_COLLECTIONS);

            if (!TextUtils.isEmpty(collectionsJson)) {
                collections = BuyClientFactory.createDefaultGson().fromJson(collectionsJson, new TypeToken<List<Product>>() {
                }.getType());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CollectionListFragmentView) inflater.inflate(R.layout.fragment_collection_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        CollectionListAdapter adapter = new CollectionListAdapter(getActivity(), theme);
        adapter.setClickListener(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Fetch the Collections if we don't have them
        if (collections == null) {
            fetchCollections();
        }
        showCollectionsIfReady();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void fetchCollections() {
        provider.getCollections(buyClient, new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections, Response response) {
                CollectionListFragment.this.collections = collections;
                showCollectionsIfReady();
            }

            @Override
            public void failure(RetrofitError error) {
                // TODO https://github.com/Shopify/mobile-buy-sdk-android-private/issues/589
            }
        });
    }

    private void showCollectionsIfReady() {
        final AppCompatActivity activity = safelyGetActivity();
        if (activity == null) {
            return;
        }

        if (!viewCreated || collections == null) {
            if (!progressDialog.isShowing()) {
                showProgressDialog(getString(R.string.loading), getString(R.string.loading_collections), new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }
            return;
        } else {
            if (progressDialog.isShowing()) {
                dismissProgressDialog();
            }
            CollectionListAdapter adapter = (CollectionListAdapter) recyclerView.getAdapter();
            adapter.setCollections(collections);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(int position, View viewHolder, Collection collection) {
        Log.i(TAG, "Collection Item clicked");
        if (listener != null) {
            listener.onItemClick(collection);
        }
    }

    @Override
    public void onItemLongClick(int position, View viewHolder, Collection collection) {
        Log.i(TAG, "Collection Item long clicked");
        if (listener != null) {
            listener.onItemLongClick(collection);
        }
    }

    public interface Listener {
        void onItemClick(Collection collection);

        void onItemLongClick(Collection collection);
    }
}
