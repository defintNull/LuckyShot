package org.luckyshot.Facades.Services.Converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import org.luckyshot.Models.Powerups.Powerup;
import org.luckyshot.Views.SinglePlayerGameView;
import org.luckyshot.Views.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PowerupConverter implements AttributeConverter<HashMap<Powerup, Integer>, String>{

    @Override
    public String convertToDatabaseColumn(HashMap<Powerup, Integer> powerupIntegerHashMap) {
        ArrayList<ArrayList<String>> matrice = new ArrayList<ArrayList<String>>();
        for(Map.Entry<Powerup, Integer> entry : powerupIntegerHashMap.entrySet()) {
            ArrayList<String> row = new ArrayList<String>();
            //Name
            row.add(entry.getKey().getClass().getName());
            //Occurences
            row.add(Integer.toString(entry.getValue()));
            matrice.add(row);
        }

        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(matrice);
        } catch (Exception e) {
            SinglePlayerGameView view = new SinglePlayerGameView();
            view.systemError();
        }

        return json;
    }

    @Override
    public HashMap<Powerup, Integer> convertToEntityAttribute(String hashMapConvertedHashMap) {
        HashMap<Powerup, Integer> hashMap = new HashMap<Powerup, Integer>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<ArrayList<String>> convertList = mapper.readValue(
                    hashMapConvertedHashMap,
                    new TypeReference<ArrayList<ArrayList<String>>>() {}
            );

            for(ArrayList<String> convert : convertList) {
                try {
                    Method method = Class.forName(convert.get(0)).getMethod("getInstance");
                    Object obj = method.invoke(null);
                    hashMap.put((Powerup) obj, Integer.valueOf(convert.get(1)));
                } catch (Exception e) {
                    SinglePlayerGameView view = new SinglePlayerGameView();
                    view.systemError();
                }
            }
        } catch (Exception e) {
            SinglePlayerGameView view = new SinglePlayerGameView();
            view.systemError();
        }
        return hashMap;
    }
}
