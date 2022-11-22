package com.application.iserv.ui.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {

    public static boolean validateNumbers(String number) {
        boolean isValid = false;

        Pattern pattern = Pattern.compile("^[0-9]{1,45}$");
        Matcher matcher = pattern.matcher(number);

        if (matcher.find()) {
            isValid = true;
        }

        return isValid;
    }

    public static String getPhoneNumberCarrier(String phone) {

        String carrier = "";
        String provider = "";

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        Phonenumber.PhoneNumber phoneNumber;

        try {
            phoneNumber = phoneNumberUtil.parse(
                    "+267"+phone,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name()
            );
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }

        PhoneNumberToCarrierMapper phoneNumberToCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

        if (!phoneNumberToCarrierMapper.getNameForNumber(phoneNumber, Locale.ENGLISH).isEmpty()) {
            carrier = phoneNumberToCarrierMapper.getNameForNumber(phoneNumber, Locale.ENGLISH);
        }

        if (carrier.equalsIgnoreCase("Orange")) {
            provider = "Orange Money";
        }
        else if (carrier.equalsIgnoreCase("Mascom")) {
            provider = "MyZaka";
        }
        else if (carrier.equalsIgnoreCase("BTC Mobile")) {
            provider = "Smega";
        }

        return provider;

    }

    public static boolean validatePhoneNumber(String phone) {

        boolean isValid = false;

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        Phonenumber.PhoneNumber phoneNumber;

        try {
            phoneNumber = phoneNumberUtil.parse(
                    phone,
                    Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED.name()
            );
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }

        PhoneNumberToCarrierMapper phoneNumberToCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

        if (!phoneNumberToCarrierMapper.getNameForNumber(phoneNumber, Locale.ENGLISH).isEmpty()) {
            isValid = true;
        }

        return isValid;

    }

}
