package com.ceri.projet;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ceri.projet.SimpleSectionedRecyclerViewAdapter.*; //Section

public class AdapterCreator {

    public static SimpleSectionedRecyclerViewAdapter createAdapterAlpha(Context context, List<Item> catalog) {

    //    créer un adapter avec les données
        RecyclerViewAdapter sectionlessAdapter = new RecyclerViewAdapter(context, catalog);
        sectionlessAdapter.sortItemsAlphabetically();

    //    créer les sections
        List<Section> sections = AdapterCreator.createSectionsAlpha(sectionlessAdapter.catalog);


        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(context,R.layout.section,R.id.section_text,sectionlessAdapter);
        adapter.setSections(sections.toArray(dummy));
        return adapter;
    }

    private static List<Section> createSectionsAlpha(List<Item> sortedCatalog) {
        List<Section> sections = new ArrayList<Section>();
        List<String> cache = new ArrayList<String>();
        for(Item item : sortedCatalog) {
            String firstLetter = AdapterCreator.getFirstLetter(item.getName()).toUpperCase();
            if(!cache.contains(firstLetter)) {
                sections.add(new Section(sortedCatalog.indexOf(item), firstLetter));
                cache.add(firstLetter);
            }
        }
        return sections;
    }

    private static String getFirstLetter(String line) {
        Pattern p = Pattern.compile("\\p{L}");
        Matcher m = p.matcher(line);
        if (m.find())
            return m.group();
        return "?";
    }
}
