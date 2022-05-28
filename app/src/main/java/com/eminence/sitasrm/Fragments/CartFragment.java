package com.eminence.sitasrm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eminence.sitasrm.Activity.CheckOutActivity;
import com.eminence.sitasrm.Activity.Profile.Address;
import com.eminence.sitasrm.Adapters.ProductAdapter;
import com.eminence.sitasrm.Interface.BadgingInterface;
import com.eminence.sitasrm.Models.AddressModel;
import com.eminence.sitasrm.Models.CartModel;
import com.eminence.sitasrm.Models.CartResponse;
import com.eminence.sitasrm.Models.ProductModel;
import com.eminence.sitasrm.R;
import com.eminence.sitasrm.Utils.DatabaseHandler;
import com.eminence.sitasrm.Utils.Helper;
import com.eminence.sitasrm.Utils.YourPreference;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.eminence.sitasrm.MainActivity.badgecountmain;
import static com.eminence.sitasrm.Utils.Baseurl.baseurl;

public class CartFragment extends Fragment implements BadgingInterface {

    RecyclerView product_recycle;
    ArrayList<ProductModel> offerlist = new ArrayList<>();
    static DatabaseHandler databaseHandler;
    LinearLayout checkOutLayout;
    static LinearLayout bottomLayout, cartEmptyLayout,productLayout;
    static TextView idtotal_amount, txt_defaultAddress;
    static TextView dis_amt, txt_discount, txtNoteAmount, txtRupeeSign;
    static TextView payble_amt;
    public static int totalprice = 0;
    LinearLayout addressLayout;
    static ImageView img_imptyCart;
    static YourPreference yourPrefrence;
    public static String subStatus = "0";
    ProgressBar progress;
    static int totalAmount = 0, discountAmount = 0, payableAmount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        product_recycle = view.findViewById(R.id.cartrecycleview);
        checkOutLayout = view.findViewById(R.id.checkOutLayout);
        bottomLayout = view.findViewById(R.id.bottomLayout);
        idtotal_amount = view.findViewById(R.id.idtotal_amount);
        progress = view.findViewById(R.id.progress);
        dis_amt = view.findViewById(R.id.dis_amt);
        payble_amt = view.findViewById(R.id.payble_amt);
        txt_defaultAddress = view.findViewById(R.id.txt_defaultAddress);
        addressLayout = view.findViewById(R.id.addressLayout);
        txt_discount = view.findViewById(R.id.txt_discount);
        cartEmptyLayout = view.findViewById(R.id.cartEmptyLayout);
        productLayout = view.findViewById(R.id.productLayout);
        img_imptyCart = view.findViewById(R.id.img_imptyCart);
        txtNoteAmount = view.findViewById(R.id.txtNoteAmount);
        txtRupeeSign = view.findViewById(R.id.txtRupeeSign);
        yourPrefrence = YourPreference.getInstance(getActivity());

        if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
            setUpDB();
            Productlist();
            getamount_from_adapter(getActivity(), progress);

            //addtocart();
        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }

        checkOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckOutActivity.class);
                intent.putExtra("total_amount", totalAmount);
                intent.putExtra("discount_amount", discountAmount);
                intent.putExtra("payable_amount", payableAmount);
                startActivity(intent);

            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Address.class);
                startActivity(intent);
            }
        });


        img_imptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Helper.INSTANCE.isNetworkAvailable(getActivity())) {
            address();
        } else {
            Helper.INSTANCE.Error(getActivity(), getString(R.string.NOCONN));
        }
    }

    public void Productlist() {
        offerlist.clear();
        progress.setVisibility(View.VISIBLE);
        String url = baseurl + "product_list";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(getActivity());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.setVisibility(View.GONE);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");

                    if (r_code.equalsIgnoreCase("1")) {
                        offerlist.clear();
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            ProductModel productModel = new ProductModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String product_id = jsonObject2.getString("product_id");
                            String category_id = jsonObject2.getString("category_id");
                            String product_name = jsonObject2.getString("product_name");
                            String price = jsonObject2.getString("price");
                            String caption_eng = jsonObject2.getString("caption_eng");
                            String caption_hindi = jsonObject2.getString("caption_hindi");
                            String pouch_quantity = jsonObject2.getString("pouch_quantity");
                            String product_image = jsonObject2.getString("product_image");
                            String created_at = jsonObject2.getString("created_at");
                            String p_name_hindi = jsonObject2.getString("product_name_hindi");
                            String description_hindi = jsonObject2.getString("description_hindi");
                            String cart_availability = jsonObject2.getString("cart_availability");
                            String quantity = jsonObject2.getString("quantity");
                            String single_description_english = jsonObject2.getString("single_description_english");
                            String single_description_hindi = jsonObject2.getString("single_description_hindi");


                            productModel.setCategory_id(category_id);
                            productModel.setProduct_id(product_id);
                            productModel.setProduct_name(product_name);
                            productModel.setPrice(price);
                            productModel.setProduct_image(product_image);
                            productModel.setPouch_quantity(pouch_quantity);
                            productModel.setCreated_at(created_at);
                            productModel.setP_name_hindi(p_name_hindi);
                            productModel.setDescription_hindi(description_hindi);
                            productModel.setCaption_eng(caption_eng);
                            productModel.setCaption_hindi(caption_hindi);
                            productModel.setQuantity(quantity);
                            productModel.setCart_availability(cart_availability);
                            productModel.setSingle_description_english(single_description_english);
                            productModel.setSingle_description_hindi(single_description_hindi);

                            //offerlist.add(productModel);

                            if (cart_availability.equals("1")) {
                                offerlist.add(productModel);
                            }
                        }

                        if (offerlist.size() == 0) {
                            bottomLayout.setVisibility(View.GONE);
                            productLayout.setVisibility(View.GONE);
                            cartEmptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            bottomLayout.setVisibility(View.VISIBLE);
                            cartEmptyLayout.setVisibility(View.GONE);
                        }

                        bindadapter(offerlist);

                    } else if (r_code.equalsIgnoreCase("100")) {
                        Toast.makeText(getActivity(), "Your Account has been Terminated!!", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences preferences = getActivity().getSharedPreferences("YourCustomNamedPreference", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear();
                                editor.apply();
                                getActivity().finishAffinity();

                            }
                        }, 1500);


                    } else {

                        product_recycle.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                //Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }

    @Override
    public void badgecount() {
        getamount_from_adapter(getActivity(), progress);
    }

    private void bindadapter(ArrayList<ProductModel> offerlist) {
        if(offerlist.size() != 0){
            ProductAdapter offerlistAdapter = new ProductAdapter(offerlist, getActivity(), this, "cartFragment", progress);
            product_recycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            product_recycle.setAdapter(offerlistAdapter);
            offerlistAdapter.notifyDataSetChanged();
        }
    }

    private void setUpDB() {
        databaseHandler = Room.databaseBuilder(getActivity(), DatabaseHandler.class, "cart").allowMainThreadQueries().build();
    }

    public void address() {
        String url = baseurl + "user_address_list";
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        YourPreference yourPrefrence = YourPreference.getInstance(requireContext());
        String id = yourPrefrence.getData("id");
        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String r_code = obj.getString("status");
                    if (r_code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            AddressModel addressModel = new AddressModel();
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String state = jsonObject2.getString("state");
                            String pincode = jsonObject2.getString("pincode");
                            String defaults = jsonObject2.getString("defaults");

                            if (defaults.equalsIgnoreCase("1")) {
                                addressModel.setState(state);
                                addressModel.setPincode(pincode);
                                txt_defaultAddress.setText("Deliver to " + state + " " + pincode);
                            }
                        }

                    } else {
                        txt_defaultAddress.setText(R.string.address2);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {

        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }


    public static void getamount_from_adapter(Context context, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        String url = baseurl + "cart_calculation";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        YourPreference yourPrefrence = YourPreference.getInstance(context);
        String id = yourPrefrence.getData("id");

        Map<String, String> params = new HashMap();
        params.put("user_id", id);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject(String.valueOf(response));
                    String status_code = obj.getString("status");
                    if (status_code.equalsIgnoreCase("1")) {
                        String total_products = obj.getString("total_products");
                        int total_Amount = obj.getInt("total_amount");
                        int discount_Amount = obj.getInt("total_discount");
                        int payAmount = total_Amount - discount_Amount;
                        if(discount_Amount == 0) {
                            txtNoteAmount.setText("Total Amount");
                            txtRupeeSign.setText("₹");
                            idtotal_amount.setText(total_Amount+"");
                        } else {
                            txtNoteAmount.setText("Total Amount - Discount = Payable Amount");
                            txtRupeeSign.setText("₹" + total_Amount + " - " + "₹" + discount_Amount + " = " + "₹");
                            idtotal_amount.setText(payAmount + "");
                        }

                        totalAmount = total_Amount;
                        discountAmount = discount_Amount;
                        payableAmount = payAmount;

                        int total_cart_item = Integer.parseInt(total_products);
                        badgecountmain(total_cart_item);

                        if (total_cart_item == 0) {
                            bottomLayout.setVisibility(View.GONE);
                            productLayout.setVisibility(View.GONE);
                            cartEmptyLayout.setVisibility(View.VISIBLE);
                        } else {
                            bottomLayout.setVisibility(View.VISIBLE);
                            cartEmptyLayout.setVisibility(View.GONE);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

            }
        }) {

        };

        requestQueue.add(stringRequest);
        stringRequest.setShouldCache(false);

    }


    @Override
    public void onStop() {
        setUpDB();
        super.onStop();
    }
}