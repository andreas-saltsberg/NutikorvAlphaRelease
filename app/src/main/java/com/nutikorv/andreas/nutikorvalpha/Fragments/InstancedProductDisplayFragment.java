package com.nutikorv.andreas.nutikorvalpha.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nutikorv.andreas.nutikorvalpha.Adapters.GridViewAdapter;
import com.nutikorv.andreas.nutikorvalpha.Objects.Basket;
import com.nutikorv.andreas.nutikorvalpha.Objects.Product;
import com.nutikorv.andreas.nutikorvalpha.Objects.Shop;
import com.nutikorv.andreas.nutikorvalpha.Parameters.GlobalParameters;
import com.nutikorv.andreas.nutikorvalpha.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by AndreasPC on 9/12/2016.
 */
public class InstancedProductDisplayFragment extends Fragment {

    Activity mActivity;

    public static InstancedProductDisplayFragment newInstance(String products) {
        InstancedProductDisplayFragment myFragment = new InstancedProductDisplayFragment();

        Bundle args = new Bundle();
        args.putString("products", products + "");
        myFragment.setArguments(args);

        return myFragment;
    }

    public InstancedProductDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_display, container, false);

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

        String productsJSON = getArguments().getString("products", "")
                .replace("\\\"", "\"");

        productsJSON = productsJSON
                .substring(1, productsJSON.length() - 1);

        Basket prods = gson.fromJson(productsJSON, Basket.class);

        List<Product> prods1 = prods.getAllHashKeys();

        RecyclerView r1 =  (RecyclerView) rootView.findViewById(R.id.recView);

        r1.setLayoutManager(new LinearLayoutManager(getContext()));

        r1.setAdapter(new MyRecyclerAdapter(getContext(), prods1));


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {
        private List<Product> products;
        private Context mContext;

        public MyRecyclerAdapter(Context context, List<Product> products) {
            this.products = products;
            this.mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.basket_cardview_item, null);

            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {
            Product p = products.get(position);

            setPriceColors(p, customViewHolder);

            customViewHolder.title.setText(p.getName());

            if (p.getImgURL().equals("0")) {
                UrlImageViewHelper.setUrlDrawable(customViewHolder.thumbnail, "http://www.jordans.com/~/media/jordans%20redesign/no-image-found.ashx?h=275&la=en&w=275&hash=F87BC23F17E37D57E2A0B1CC6E2E3EEE312AAD5B");
            } else {
                UrlImageViewHelper.setUrlDrawable(customViewHolder.thumbnail, p.getImgURL());
            }
        }

        @SuppressWarnings("ResourceType")
        private void setPriceColors(Product currentProduct, CustomViewHolder childViewHolder) {
            childViewHolder.selverPiecePrice.setBackgroundResource(R.color.colorPrimaryDark);
            childViewHolder.maximaPiecePrice.setBackgroundResource(R.color.colorPrimaryDark);
            childViewHolder.prismaPiecePrice.setBackgroundResource(R.color.colorPrimaryDark);


            List<TextView> textViews = new ArrayList<>(Arrays.asList(
                    childViewHolder.selverPiecePrice, childViewHolder.prismaPiecePrice, childViewHolder.maximaPiecePrice));
            List<Shop> shops = currentProduct.getShops();
            Collections.sort(shops, new Comparator<Shop>() {
                @Override
                public int compare(Shop lhs, Shop rhs) {
                    if (Double.compare(lhs.getPrice(), rhs.getPrice()) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });

            for (int i = 0; i < textViews.size(); i++) {
                textViews.get(i).setText(shops.get(i).toString()); // SHOPS LENGTH MUST EQUAL TEXTVIEWS LENGTH
                textViews.get(i).setVisibility(shops.get(i).getVisibilityValue());

                if (shops.get(i).isOnSale()) {
                    textViews.get(i).setBackgroundResource(R.color.colorPrimaryLight);
                }
            }

        }


        @Override
        public int getItemCount() {
            return products.size();
        }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title;
        private TextView price;
        private ImageButton button;
        private TextView quantity;
        private TextView selverPiecePrice;
        private TextView prismaPiecePrice;
        private TextView maximaPiecePrice;

        public CustomViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.productImage);
            this.thumbnail.setAnimation(null);
            this.title = (TextView) view.findViewById(R.id.productName);
            this.button = (ImageButton) view.findViewById(R.id.deleteProduct);
            this.price = (TextView) view.findViewById(R.id.totalPrice);
            this.quantity = (TextView) view.findViewById(R.id.quantity);
            this.maximaPiecePrice = (TextView) view.findViewById(R.id.maximaPrice);
            this.selverPiecePrice = (TextView) view.findViewById(R.id.selverPrice);
            this.prismaPiecePrice = (TextView) view.findViewById(R.id.prismaPrice);
        }
    }
    }

}