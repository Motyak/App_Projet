package com.ceri.projet;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ceri.projet.SimpleSectionedRecyclerViewAdapter.*; //Section

public class AdapterCreator {

    public static SimpleSectionedRecyclerViewAdapter createAdapterCategories(Context context, List<String> categories, List<Item> catalog) {
        //    créer les données et sections
        List<Item> itemsSortedByCategories = new ArrayList<>();
        List<Section> sections = new ArrayList<>();
        for(String str : categories) {
            sections.add(new Section(itemsSortedByCategories.size(), str));
            for(Item item : catalog) {
                if(item.getCategories().contains(str))
                    itemsSortedByCategories.add(item);
            }
        }

        RecyclerViewAdapter sectionlessAdapter = new RecyclerViewAdapter(context, itemsSortedByCategories);

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(context,R.layout.section,R.id.section_text,sectionlessAdapter);
        adapter.setSections(sections.toArray(dummy));
        return adapter;
    }

    public static SimpleSectionedRecyclerViewAdapter createAdapterChrono(Context context, List<Item> catalog) {
        //    créer un adapter avec les données
        RecyclerViewAdapter sectionlessAdapter = new RecyclerViewAdapter(context, catalog);
        sectionlessAdapter.sortItemsChronologically();

        //    créer les sections
        List<Section> sections = AdapterCreator.createSectionsChrono(sectionlessAdapter.catalog);


        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(context,R.layout.section,R.id.section_text,sectionlessAdapter);
        adapter.setSections(sections.toArray(dummy));
        return adapter;
    }

    private static List<Section> createSectionsChrono(List<Item> sortedCatalog) {
        List<Section> sections = new ArrayList<Section>();
        List<Integer> cache = new ArrayList<Integer>();
        for(Item item : sortedCatalog) {
            Integer year = item.getYear();
            if(!cache.contains(year)) {
                sections.add(new Section(sortedCatalog.indexOf(item), String.valueOf(year)));
                cache.add(year);
            }
        }
        return sections;
    }

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
