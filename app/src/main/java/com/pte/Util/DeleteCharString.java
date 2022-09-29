package com.pte.Util;

public class DeleteCharString {
    public String deleteCharString(String sourceString, char chElemData) {
        String deleteString = "";
        int iIndex = 0;
        for (int i = 0; i < sourceString.length(); i++) {
            if (sourceString.charAt(i) == chElemData) {
                if (i > 0) {
                    deleteString += sourceString.substring(iIndex, i);
                }
                iIndex = i + 1;
            }
        }
        if (iIndex <= sourceString.length()) {
            deleteString += sourceString.substring(iIndex, sourceString.length());
        }
        return deleteString;
    }
}
