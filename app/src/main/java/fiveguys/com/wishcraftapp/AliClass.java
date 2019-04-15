//package fiveguys.com.wishcraftapp;
//
//import android.util.Log;
//
//import com.aliseeks.*;
//import com.aliseeks.auth.*;
//import com.aliseeks.models.*;
//import com.aliseeks.ProductsApi;
//
//import java.io.File;
//import java.util.*;
//
//
//public class AliClass {
//
//    final private static String API_KEY = "YNZBUIVFBSOPLNSO";
//    public class ProductsApiExample {
//
//        public static void main(String[] args) {
//            ApiClient defaultClient = Configuration.getDefaultApiClient();
//
//            // Configure API key authorization: ApiKeyAuth
//            ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("ApiKeyAuth");
//            ApiKeyAuth.setApiKey(AliClass.API_KEY);
//            // Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//            //ApiKeyAuth.setApiKeyPrefix("Token");
//
//            ProductsApi apiInstance = new ProductsApi(defaultClient);
//            ProductRequest productRequest = new ProductRequest(); // ProductRequest | The request body of get product
//            SearchItem search = new SearchItem();
//            search.title("beats");
//            try {
//                Product result = apiInstance.getProduct(productRequest);
//                Log.wtf("Ali", result.toString());
//                //System.out.println(result);
//            } catch (ApiException e) {
//                Log.e("Ali", "something wen't wrong");
//                //System.err.println("Exception when calling ProductsApi#getProduct");
//                //e.printStackTrace();
//            }
//        }
//    }
//
//}
