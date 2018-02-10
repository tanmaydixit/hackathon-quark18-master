package com.example.shreyas.hackathon_quark18;

import java.util.List;



public class PartnerItemFormat {
    String name;
    String imageurl;
    List<OfferItemFormat> offers;

    public PartnerItemFormat(String name, String imageurl, List<OfferItemFormat> offerItemFormats) {
        this.name = name;
        this.imageurl = imageurl;
        this.offers = offerItemFormats;
    }

    public PartnerItemFormat() {
    }

    public List<OfferItemFormat> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferItemFormat> offers) {
        this.offers = offers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}