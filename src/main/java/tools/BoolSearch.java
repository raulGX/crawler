package tools;

import db.DirectIndexCollectionBridge;
import db.IndirectIndexCollectionBridge;
import org.bson.Document;

import java.util.*;

public class BoolSearch {
    private HashSet<String> set;
    private HashMap<String,HashMap<String, Double>> index;
    private List<String> words;
    private List<String> bools;
    public BoolSearch() {
        set = new HashSet<>();
        index = new HashMap<>();
    }

//{
//	"_id" : "tea",
//	"values" : [
//		{
//			"no" : 3,
//			"path" : "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/testfolder/1.txt"
//		},
//		{
//			"no" : 1,
//			"path" : "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/testfolder/3.txt"
//		}
//	]
//}
//
    private HashMap<String, Double> getMapFromArray(ArrayList<Document> docs) {
        HashMap<String, Double> retMap = new HashMap<>();
        for (Document doc : docs) {
            retMap.put((String)doc.get("path"), (Double)doc.get("no"));
        }
        return  retMap;
    }

    private void getWordIndexes() {
        for (String word : words) {
            Document doc = IndirectIndexCollectionBridge.getWord(word);
            HashMap<String, Double> indirectIndex = getMapFromArray((ArrayList<Document>) doc.get(IndirectIndexCollectionBridge.DocumentValue));
            index.put(word, indirectIndex);
        }
    }

    public HashSet<String> boolSearch(String searchString) {
        words = WordParser.getWordsForBoolSearch(searchString, false);
        bools = WordParser.getWordsForBoolSearch(searchString, true);

        //adds directories from first item to the hashset

        //for every word do this make index
        getWordIndexes();
        set.addAll(index.get(words.get(0)).keySet());
        words.remove(0);

        for (String b : bools) {
            switch (b) {
            case "and":
                HashMap<String, Double> nextMap = index.get(words.get(0));
                words.remove(0);
                filterAnd(nextMap);
                break;
            case "or":
                HashMap<String, Double> nextMapOr = index.get(words.get(0));
                words.remove(0);
                filterOr(nextMapOr);
                break;
            case "not":
                HashMap<String, Double> nextMapNot = index.get(words.get(0));
                words.remove(0);
                filterNot(nextMapNot);
                break;
            }
        }

        return set;
    }

    private void filterAnd(HashMap<String, Double> nextMap) {
        if (nextMap == null){
            set = new HashSet<>(); //multime and empty = empty
            return;
        }
        if (nextMap.size() > set.size()) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!nextMap.containsKey(key)) {
                    it.remove();
                }
            }
        } else {
            HashSet<String> oldSet = set;
            set = new HashSet<>();
            Iterator it = nextMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (oldSet.contains(key)) {
                    set.add(key);//test
                }
            }
        }
    }

    private void filterOr(HashMap<String, Double> nextMap) {
        if (nextMap == null)
            return;
        HashSet<String> newSet = new HashSet<>();

        if (nextMap.size() > set.size()) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!nextMap.containsKey(key)) {
                    newSet.add(key);
                }
            }
            newSet.addAll(nextMap.keySet());
        } else {
            Iterator it = nextMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (!set.contains(key)) {
                    newSet.add(key);
                }
            }
            newSet.addAll(set);
        }

        set = newSet;
    }

    private void filterNot(HashMap<String, Double> nextMap) {
        if (nextMap == null)
            return;
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            if (nextMap.containsKey(it.next())) {
                it.remove();
            }
        }
    }
}
