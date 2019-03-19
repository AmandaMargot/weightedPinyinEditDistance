import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.dictionary.py.Shengmu;
import com.hankcs.hanlp.dictionary.py.Yunmu;


public class weightedPinyinEditDistance {
    private double distance;

    public weightedPinyinEditDistance(String s1, String s2){
        this.distance = calculateWeightedPinyinEditDistance(s1,s2);
    }


    public double getDistance(){
        return this.distance;
    }


    private double calculateWeightedPinyinEditDistance(String s1, String s2) {
        List<Pinyin> pinyinList1 = HanLP.convertToPinyinList(s1);
        List<Pinyin> pinyinList2 = HanLP.convertToPinyinList(s2);
        int len1 = pinyinList1.size();
        int len2 = pinyinList2.size();

        double[][] dp = new double[len1 + 1][len2 + 1];
        double distance = 0;

        if (len1 == 0 && len2 == 0) { return 0; }
        if(len1 > 0 && len2 == 0){return pinyinList1.size();}
        if(len1 == 0 && len2 >0){return pinyinList2.size();}

        for (int i = 0; i < len1+1; i++) {
            dp[i][0] = distance++;
        }

        distance = 0;
        for (int i = 0; i < len2+1; i++) {
            dp[0][i] = distance++;
        }

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                Pinyin pinyin1 = pinyinList1.get(i-1);
                Pinyin pinyin2 = pinyinList2.get(j-1);

                double distance3 = getDistance(pinyin1, pinyin2);
                double temp1 = dp[i - 1][j] + 1;
                double temp2 = dp[i][j - 1] + 1;
                double temp3 = dp[i - 1][j - 1] + distance3;
                dp[i][j] = Math.min(temp3, Math.min(temp1, temp2));
            }
        }
        return (double) dp[len1][len2];
    }


    public double getDistance(Pinyin pinyin1, Pinyin pinyin2) {

        Map<Shengmu, Shengmu> shengmuMap = loadApproximateShengmuAsMap();
        Map<Yunmu, Yunmu> yunmuMap = loadApproximateYunmuAsMap();

        int tone1 = pinyin1.getTone();
        int tone2 = pinyin2.getTone();
        Shengmu shengmu1 = pinyin1.getShengmu();
        Shengmu shengmu2 = pinyin2.getShengmu();
        Yunmu yunmu1 = pinyin1.getYunmu();
        Yunmu yunmu2 = pinyin2.getYunmu();

        double distance = 0;

        if (pinyin1.getPinyinWithoutTone().equals(pinyin2.getPinyinWithoutTone())) {
            if (tone1 == tone2) {
                distance = 0;
            } else { distance = 0.5; }
        } else {
            if (shengmu1 == shengmu2 && yunmu1 != yunmu2) {
                if (yunmuMap.containsKey(yunmu1)) {
                    Yunmu matchedYunmu = yunmuMap.get(yunmu1);
                    if (matchedYunmu == yunmu2) {
                        distance = 0.5;
                    }
                    else {
                        distance = 1;
                    }
                } else {
                    distance = 1;
                }
            } else if (yunmu1 == yunmu2 && shengmu1 != shengmu2) {
                if (shengmuMap.containsKey(shengmu1)) {
                    Shengmu matchedShengmu = shengmuMap.get(shengmu1);
                    if (matchedShengmu == shengmu2) {
                        distance = 0.5;
                    } else {
                        distance = 1;
                    }
                } else {
                    distance = 1;
                }
            } else {
                if (shengmuMap.containsKey(shengmu1)){
                    Shengmu matchedShengmu = shengmuMap.get(shengmu1);
                    if (matchedShengmu == shengmu2){
                        distance = 0.5;
                    }
                    else {
                        distance = 2;
                    }
                }
                else if (yunmuMap.containsKey(yunmu1)){
                    Yunmu matchedYunmu = yunmuMap.get(yunmu1);
                    if (matchedYunmu == yunmu2){
                        distance = 0.5;
                    }
                    else {
                        distance = 2;
                    }
                }
                else {
                    distance = 2;
                }
            }
        }
        return distance;
    }


    private Map<Shengmu, Shengmu> loadApproximateShengmuAsMap() {

        Map<Shengmu, Shengmu> shengmuMap = Maps.newHashMap();
        shengmuMap.put(Shengmu.l, Shengmu.n);
        shengmuMap.put(Shengmu.n, Shengmu.l);
        shengmuMap.put(Shengmu.z, Shengmu.zh);
        shengmuMap.put(Shengmu.zh, Shengmu.z);
        shengmuMap.put(Shengmu.c, Shengmu.ch);
        shengmuMap.put(Shengmu.ch, Shengmu.c);
        shengmuMap.put(Shengmu.s, Shengmu.sh);
        shengmuMap.put(Shengmu.sh, Shengmu.s);
        shengmuMap.put(Shengmu.f, Shengmu.h);
        shengmuMap.put(Shengmu.h, Shengmu.f);
        return shengmuMap;
    }


    private Map<Yunmu, Yunmu> loadApproximateYunmuAsMap() {
        Map<Yunmu, Yunmu> yunmuMap = Maps.newHashMap();
        yunmuMap.put(Yunmu.in, Yunmu.ing);
        yunmuMap.put(Yunmu.ing, Yunmu.in);
        yunmuMap.put(Yunmu.en, Yunmu.eng);
        yunmuMap.put(Yunmu.eng, Yunmu.en);
        yunmuMap.put(Yunmu.an, Yunmu.ang);
        yunmuMap.put(Yunmu.ang, Yunmu.an);
        yunmuMap.put(Yunmu.uang, Yunmu.uan);
        yunmuMap.put(Yunmu.uan, Yunmu.uang);
        return yunmuMap;
    }
}


