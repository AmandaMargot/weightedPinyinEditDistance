# weightedPinyinEditDistance

基于拼音改良的编辑距离
Improved edit distance for Pinyin 


原论文：《基于拼音相似度的汉语模糊检索方法的研究》
作者：曹犟


Original paper: Research on Chinese Approximate Retrieval Methods Based on Pinyin Similarity
Authoer: Jiang Cao


计算方法：
1. 发音相似的声母或韵母差异小于1，如/l/和/n/, /z/和/zh/, /c/和/ch/, /s/和/sh/, /f/和/h/, /in/和/ing/, /in/和/ing/, /en/和/eng/, /an/和/ang/, /uan/和/uang/
2. 若同一音节的声母和韵母同时发生改变，则在计算编辑距离时给予一个正的惩罚值
3. 音调变化导致的差异小于1

