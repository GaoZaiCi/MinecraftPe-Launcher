//
// Decompiled by Jadx - 518ms
//
package com.mojang.minecraftpe.store.googleplay;

import androidx.annotation.Keep;

import com.mojang.minecraftpe.MainActivity;
import com.mojang.minecraftpe.store.ExtraLicenseResponseData;
import com.mojang.minecraftpe.store.Store;
import com.mojang.minecraftpe.store.StoreListener;

@Keep
public class GooglePlayStore implements Store {
    MainActivity mActivity;
    StoreListener mListener;

    public void acknowledgePurchase(String str, String str2) {
    }

    public void destructor() {
    }

    public void purchase(String str, boolean z, String str2) {
    }

    public void purchaseGame() {
    }

    public void queryProducts(String[] strArr) {
    }

    public void queryPurchases() {
    }

    public GooglePlayStore(MainActivity mainActivity, String str, StoreListener storeListener) {
        this.mActivity = mainActivity;
        this.mListener = storeListener;
        this.mListener.onStoreInitialized(true);
    }

    public String getStoreId() {
        return "android.googleplay";
    }

    public boolean hasVerifiedLicense() {
        return true;
    }

    public String getProductSkuPrefix() {
        return "";
    }

    public String getRealmsSkuPrefix() {
        return "";
    }

    public boolean receivedLicenseResponse() {
        return true;
    }

    public ExtraLicenseResponseData getExtraLicenseData() {
        return new ExtraLicenseResponseData(60000, 0, 0);
    }
}
