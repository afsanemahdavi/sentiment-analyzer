# A semi-supervised approach for sentiment analysis of text in social media

One of the subjects that significantly affects decision making , is awareness of feelings and
opinions of others. Sentiment analysis and opinion mining, is the process of extracting opinions
and feelings from textual data which is one of the challenging researches in computer science,
data mining and natural language processing.

We propose a semi-supervised approach for sentiment analysis and we are motivated by Persian language case study.

We've defined some special `Part-Of-Speech` tags along with a custom specialized model in the first step and then create a platform with structural rules which is called standardization process.

We focused syntactic structures of sentences and assumed that these structures have a key role in describing general concept of sentences. 
In order to extract syntactic features, we've used supervised approaches to extract these structures from sentences.

Some times texts in social medias are subject to some transformations in syntax structure and written words. Therefore, we suggest defining custom rules based on language structure. These rules should be generated to convert such syntax transformations into standard grammatical form. The general diagram of our designed system is as following

<img src="https://raw.githubusercontent.com/afsanemahdavi/sentiment-analyzer/master/f1.png">

<br>

<img src="https://raw.githubusercontent.com/afsanemahdavi/sentiment-analyzer/master/f2.png">

<br>
Our approach is composed of the following steps: 
1. feature extraction
2. syntactic standardization
3. syntactic categorization
4. calculation of the sentiment score. 

Before features extraction phase, some normalization steps are applied for better results. All of these stages are parallel. Also for a
large input, sentence group based buckets of input data can be parallelized using a computing cluster like `Hadoop`.

## Normalization phase

Most of the time, in social media and forums, user generated contents are not very well
formatted. We noticed multiple miss formatted sentences such as bad spacing between words and
misplaced punctuation marks in our samples. These miss formatted contents makes POS 2 taggers
to generate wrong outputs.
Besides, in such contents, sentences not always split by punctuation marks. This may lead to
wrong results. A strong positive sentence followed by a weak negative sentence without
punctuation marks, causes a strong negative result which is wrong. Three normalization stages
are assumed in our system as below:

1. Splitting a comment to independent sentences according to punctuation marks
such as the end point, new line, question mark and other things to maximize the accuracy
of the POS tagging results.
2. Splitting sentences based on verbs for better sentence grouping.
3. Reconstructing misformed compound words for better sentiment analyzing.
The second and third stages are done after POS tagging.

## Feature Extraction Sub System

This stage is very important, because our purpose is sentiment calculation based on syntactic
features. For Persian syntactic feature extraction, we use POS tags. All limited corpora for
Persian such as Bijankhan and Hamshahri, have been created for book texts, so using them
for social media (colloquial) texts, causes miscalculated tags and unusable results.

This is due to the transformations in the colloquial words and the missing space, half-space and
transposed position of words by the users. For this purpose, we create a corpus of colloquial
Persian sentences manually.

Sentences had to be chosen in a way that not only cover the standard words along with their
colloquial forms and their different situations of placement, but also tagged non-standard forms.

Thus, later in standardization process, by applying these tags and defined rules, these forms are
converted to standard format. Therefore, we define new tags based on the sentence structure in
our corpus. For example, in Persian, a verb is negated using some prefixes. This prefix is added
to verb using a pseudo-space in standard form. In forums and social media, people almost never
consider pseudo-spaces. So we had to implement a special tag to cover this kind of misformed
verbs to find out if the verb is negative or positive.

A prefix or suffix can be used by complete-space, intra-word space or can be connected to root
word in Persian language. Some of these prefixes or postfixes change a subjective word into an
objective one. For example a noun can be changed into adjective. The important point is
identification of compound word in Persian.
Because of the lack of using proper intra-word spacing, existing natural language processing
tools, misunderstand them as nouns and prepositions and dump them out. So, some important
objective words won’t be considered.
To solve this, we created some extended POS tags. Using those tags, for a compound like:

`PO+SPACE+NOUN=ADJECTIVE`

We tagged the `PO` as `ADJ_SAZ_PI` and the noun as `ADJ`.

Later we use these tags in our normalization tool to reconstruct the whole compound as an
adjective.
We created all of these abnormal forms in our POS training set and tagged them accordingly. It
is also figured out having so many sentences does not necessarily make an accurate tagging
output.

The key to the better tagging results was to cover all the different forms of verbs, adverbs and
adjectives that people may use for their sentences with pretty simple and short sentences in our
training set. Then we had to cover other forms of the sentences like starting a sentence with verb,
ending a sentence with verb and so on. We had to repeat this formats with multiple various
verbs, adverbs, adjective and nouns to prevent tagger to judge by word’s place in sentences.
Then we had to have multiple complex and long sentences containing multiple nouns, adverbs
and other things to make a better corpus.

## Normalization (1)

Splitting independent sentences to sub-sentences according to structure of verbs and adverbs.
We assume two types of verbs; if there is a preposition after a verb, sentence isn’t independent
and the meaning isn’t complete. If there isn’t any preposition after verb, meaning of the sentence
is complete and two parts of a sentence are separated by verb using split function at this stage.

## Normalization (2)

With the help of POS result and extended tags, we attempt to reconstruct misformed compounds
with the following algorithm:

...

## Grouping phase

In this phase, we created a rule based framework (SentiMarker4) that with a traversal algorithm
accepts some rules and creates groups of dependency structure. We define some dependency
rules for Persian language, specialized for sentiment grouping.
Based on this structure, components which wouldn’t affect the sentiment polarity specified as
unnecessary input in the rules and will be omitted by SentiMarker4. For Other components, we
define some rules according to below parameters:
(1) Tag (2) type of application (3) classification (4) direction (5) degree of classification.
Definition of grouping rules:In natural languages, a sentence is formed of some parts that together, create a complete
meaning. Some of these parts are more important than others and without them, we can’t
understand the sentence clearly. This is also true for each part. Meaning, usually in each parts
some words are more important than others. This leads to a hierarchy of words and groups with a
root and some related words and root word is the most important word in the sentence.
Based on this idea, we define our own rules to generate this hierarchy structure and call it
dependency grouping and the rules will be called dependency rules.

...

Here is a sample of how tags can be defined:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<tags 
	tags_splitter="\|" prefdir="-" dictionary_loc="sentiment_dictinary.txt" dictionary_spliter=":">
	
	<tag imp="true">
		<name>ADJ</name>
	</tag>
	<tag ingroupcount="1" imp="true">
		<name>ADV</name>
		<groups dir="+" count="1" opt="*">ADJ</groups>
	</tag>
	<tag imp="true">
		<name>V_PA</name>
		<groups dir="-" count="-1" opt="*"/>
	</tag>
	<tag imp="true">
		<name>V_PR</name>
		<groups dir="-" count="-1" opt="*"/>
	</tag>
	<tag>
		<name>ADJ_SAZ_PA</name>
		<joinsto dir="-">ADJ</joinsto>
	</tag>
	<tag>
		<name>ADJ_SAZ_PI</name>
		<joinsto dir="+">ADJ</joinsto>
	</tag>
	<tag imp="true">
		<name>V_PI</name>	
		<!--<joinsto dir="+">V_PA</joinsto>
		<joinsto dir="+">V_PR</joinsto>-->
	</tag>
	<tag>
		<name>V_PIO</name>
		<joinsto dir="+" gap=" ">V_PA</joinsto>
		<joinsto dir="+" gap=" ">V_PR</joinsto>
	</tag>
	<tag imp="true">
		<name>CON</name>
		<groups dir="+" count="-1" opt="*"/>
	</tag>
	<tag>
		<name>ADJ_PI</name>
		<joinsto dir="+">ADJ</joinsto>
	</tag>
	<tag>
		<name>ADJ_PA</name>
		<joinsto dir="-">ADJ</joinsto>
	</tag>
	<tag>
		<name>PRO</name>
	</tag>
	<tag>
		<name>PO</name>
	</tag>
	<tag>
		<name>N</name>
	</tag>
</tags>
```

## Score calculation

The sentiment lexicon is one of the crucial resources for most sentiment analysis algorithms.
Because of the lack of sentiment lexicon in Persian, a sentiment lexicon which contains of 3400
Persian words including adjectives, adverbs, negotiating words and others with their scores was
generated.Finally with the help of our generated lexicon, proposed system calculates score of each
group. The addition and multiplication operators are defined for each group.

Then, recursively begins to calculate the sentiment degree of whole document by sum up all scores.
Finally verb prepositions combined with verbs and conjunctions, classified their front words.
Hence, the words inside categories added together and multiplied by their root word. 

If the categories are placed inside other categories, their values will be added together and multiplied
again by the score of root. 

This way, we have the final strength and polarity of the corresponding
groups and at the end, by multiplying to the ultimate root, the final polarity and sentiment score
of the whole sentence will be calculated and a sentence with many positive adjectives and a
negative verb would not cause a positive result.

Now that the software has calculated the sentiment of all the sentences, it’ll add up all the scores
to calculate ultimate sentiment result of a comment in a map reduce paradigm.

Trained model, Xml based rules, the sentiment lexicon and the input file passed to the
SentiMarker4 to calculate final score of each comment.

In order to have a closer look at the results, we show analyzing process of one of the sentences
from our input data.
```
[ ‫خخخخخ‬
٠٨:٥٤ | ٠٥ / ٠٤ / ١٣٩٣ ] ‫ترین‬ ‫بد‬ ‫بوسنی‬ ‫مقابل‬ ‫در‬ ‫ولی‬ ‫درخشید‬ ‫خوب‬ ‫خیلی‬ ‫ارزانتین‬ ‫با‬ ‫دربازی‬ ‫ایران‬
‫ببازد‬ ‫ایران‬ ‫نمیکرد‬ ‫باور‬ ‫کس‬ ‫ھیچ‬ ‫اصلا‬ ‫کھ‬ ‫تیمی‬ ‫مقابل‬ ‫در‬ ‫داشت‬ ‫رو‬ ‫بازی‬
‫بودحد‬ ‫فشرده‬ ‫ما‬ ‫امتحانی‬ ‫برنامھ‬ ‫زیرا‬ ‫داشتیم‬ ‫رو‬ ‫شرایط‬ ‫ترین‬ ‫سخت‬ ‫جھانی‬ ‫جام‬ ‫این‬ ‫خاطر‬ ‫بھ‬ ‫ایران‬ ‫اموزان‬ ‫دانش‬ ‫ی‬ ‫وھمھ‬ ‫من‬
‫باشید‬ ‫سلامت‬ ‫ھمیشھ‬ ‫انشاءالله‬ ‫اوردین‬ ‫می‬ ‫خوب‬ ‫نتیجھ‬ ‫یھ‬ ‫اقل‬
‫سلامتیشون‬ ‫بھ‬ ‫ھامون‬ ‫والیبالی‬ .... ‫فقط‬ ... ‫فقط‬ .... ‫فقط‬
```
In the given sentences, user “‫”خخخخخ‬ at the given time commented about Iran versus Bosnia
FIFA World cup 2014.

Given comment in Persian is roughly translated to English as following:

Iran is very well played against Argentina but had the worst performance against Bosnia.
Againsta team that no one could believe to lose, students had been in the worst situation because of their
exams pressure and they expected you could do your best. 
I wish the bests for you all just our volleyball team had the best performance

The overall result calculated by SentiMarker4 is +6 point.

To see how this result is achieved, POS tags and grouping outputs and finally assigned weights should be analyzed.

POS tagging output is as following:
```
‫‪|N‬ایران‬ ‫‪|N‬دربازی‬ ‫‪|PO‬با‬ ‫‪|N‬ارزانتین‬ ‫‪|ADV‬خیلی‬ ‫‪|ADJ‬خوب‬ ‫‪|V_PA‬درخشید‬
‫)‪2‬‬
‫‪|CON‬ولی‬ ‫‪|PO‬در‬ ‫‪|N‬مقابل‬ ‫‪|N‬بوسنی‬ ‫‪|ADJ‬بد‬ ‫‪|ADJ_SAZ_PA‬ترین‬ ‫‪|N‬بازی‬ ‫‪|PO‬رو‬ ‫‪|V_PA‬داشت‬
‫‪|PO‬در‬ ‫‪|N‬مقابل‬ ‫‪|N‬تیمی‬ ‫‪|CON‬کھ‬ ‫‪|ADV‬اصلا‬ ‫‪|N‬ھیچ‬ ‫‪|N‬کس‬ ‫‪|ADJ‬باور‬ ‫‪|V_PA‬نمیکرد‬
‫)‪3‬‬
‫‪|N‬ایران‬ ‫‪|V_PR‬ببازد‬
‫)‪4‬‬
‫‪|PRO‬من‬ ‫‪|N‬وھمھ‬ ‫‪|PO‬ی‬ ‫‪|N‬دانش‬ ‫‪|N‬اموزان‬ ‫‪|N‬ایران‬ ‫‪|PO‬بھ‬ ‫‪|PO‬خاطر‬ ‫‪|PO‬این‬ ‫‪|N‬جام‬ ‫‪|ADJ‬جھانی‬ ‫‪|ADJ‬سخت‬
‫‪|ADJ_SAZ_PA‬ترین‬ ‫‪|N‬شرایط‬ ‫‪|PO‬رو‬ ‫‪|V_PA‬داشتیم‬
‫‪|N‬زیرا‬ ‫‪|N‬برنامھ‬ ‫‪|ADJ‬امتحانی‬ ‫‪|PRO‬ما‬ ‫‪|ADJ‬فشرده‬ ‫‪|V_PA‬بودحد‬
‫)‪4‬‬
‫‪|N‬اقل‬ ‫‪|NO‬یھ‬ ‫‪|N‬نتیجھ‬ ‫‪|ADJ‬خوب‬ ‫‪|V_PIO‬می‬ ‫‪|V_PA‬اوردین‬
‫)‪5‬‬
‫‪|N‬انشاءالله‬ ‫‪|N‬ھمیشھ‬ ‫‪|V_PI‬سلامت‬ ‫‪|V_PR‬باشید‬
‫)‪6‬‬
‫‪|ADV‬فقط‬
‫)‪7‬‬
‫‪|ADV‬فقط‬
‫)‪8‬‬
‫‪|ADV‬فقط‬
‫)‪9‬‬
```
In the given output, sentences have been separated based on verbs. 
`ADJ_SAZ_PA` that indicates compound adjective has been detected very well and at the next steps they will be joined to create normalized output.

Next in the grouping stage, we have:
```
V_PA[‫(]درخشید‬ADV[‫(]خیلی‬ADJ(‫))خوب‬N[‫)(]ارزانتین‬N[‫)(]دربازی‬N[‫))(]ایران‬
V_PA[‫(]داشت‬CON[‫(]ولی‬N[‫)(]مقابل‬N[‫)(]بوسنی‬ADJ[ ‫ترین‬ ‫بد‬ ]()N[‫)))(]بازی‬
V_PA[‫(]نمیکرد‬CON[‫(]کھ‬ADV[‫(]اصلا‬ADJ(‫))باور‬N[‫)(]ھیچ‬N[‫))(]کس‬N[‫)(]تیمی‬N[‫))(]مقابل‬
V_PR[‫(]ببازد‬N[‫))(]ایران‬
V_PA[‫(]داشتیم‬N[‫)(]شرایط‬ADJ[‫سخت‬
‫)(]ترین‬ADJ[‫)(]جھانی‬N[‫)(]جام‬N[‫)(]ایران‬N[‫)(]اموزان‬N[‫)(]دانش‬N[‫))(]وھمھ‬
V_PA[‫(]بودحد‬ADJ[‫)(]فشرده‬ADJ[‫)(]امتحانی‬N[‫)(]برنامھ‬N[‫))(]زیرا‬
V_PA[ ‫اوردین‬ ‫می‬ ](ADJ[‫)(]خوب‬N[‫)(]نتیجھ‬N[‫))(]اقل‬
V_PR[‫(]باشید‬V_PI[‫(]سلامت‬N[‫)(]ھمیشھ‬N[‫)))(]انشاءالله‬
ADV[‫)(]فقط‬
ADV[‫)(]فقط‬
ADV[‫)(]فقط‬
ADJ[‫)(]والیبالی‬N[‫)(]سلامتیشون‬
```
Compound adjectives have been joined together
```
( ‫ترین‬ ‫بد‬ -> ‫)بدترین‬.
```
Also names and prepositions have been removed due to the subjective nature of them.
Then weighting phase with the help of sentiment dictionary is as following: 
(10000 means couldnot find proper score for the given word. They will be ineffective in the final calculations)
```
V_PA[1]*(ADV[2]*(ADJ[2]+)=4+N[10000]+()=10000+N[10000]+()=10000+N[10000]+()=100
00+)=4+V_PA[10000]*(CON[10000]*(N[10000]+()=10000+N[10000]+()=10000+ADJ[-4]+()=-
4+N[10000]+()=10000+)=-4+)=-4+
V_PA[-
1]*(CON[10000]*(ADV[2]*(ADJ[2]+)=4+N[10000]+()=10000+N[10000]+()=10000+)=4+N[10
000]+()=10000+N[10000]+()=10000+)=-4+
V_PR[-1]*(N[10000]+()=10000+)=-1+
V_PA[10000]*(N[10000]+()=10000+ADJ[10000]+()=10000+ADJ[10000]+()=10000+N[10000]
+()=10000+N[10000]+()=10000+N[10000]+()=10000+N[10000]+()=10000+N[10000]+()=1000
0+)=10000+
V_PA[10000]*(ADJ[-1]+()=-
1+ADJ[10000]+()=10000+N[10000]+()=10000+N[10000]+()=10000+)=-1+
V_PA[10000]*(ADJ[2]+()=2+N[1]+()=1+N[10000]+()=10000+)=3+
V_PR[10000]*(V_PI[1]*(N[10000]+()=10000+N[10000]+()=10000+)=1+)=1+
ADV[10000]*()=10000+
ADV[10000]*()=10000+
ADV[10000]*()=10000+
ADJ[10000]+()=10000+N[10000]+()=10000+
```
First statement of the comment admires previous Iran and Argentina game:
```
V_PA[1]*(ADV[2]*(ADJ[2]+)=4+N[10000]+()=10000+N[10000]+()=10000+N[10000]+()=100
00+)=4+
```
Next statement complains the next game result with Bosnia.
```
V_PA[10000]*(CON[10000]*(N[10000]+()=10000+N[10000]+()=10000+ADJ[-4]+()=-
4+N[10000]+()=10000+)=-4+)=-4+
```
The overall sentiment score for these two sentences will be calculated as 0.
In the next phrase, author is deeply wondering how Iran team lost the game to Bosnia.1)
```
V_PA[-1]*(CON[10000]*(ADV[2]*(ADJ[2]+)=4+N[10000]+()=10000+N[10000]+()=10000+)=4+N[10000]+()=10000+N[10000]+()=10000+)=-4+2)V_PR[-1]*(N[10000]+()=10000+)=-1+
```
As you noticed, the whole sentence has been divided into separate parts for calculation and this
is the benefit of separating sentences based on verbs. 
If it was not, the overall score would be calculated as +4 due to multiplying by -1.

For the next phrase, the author is complaining how it was hard for students to schedule they
school programs and manage their exams to due to their attentions to the competitions.
```
V_PA[10000]*(N[10000]+()=10000+ADJ[10000]+()=10000+ADJ[10000]+()=10000+N[10000]
+()=10000+N[10000]+()=10000+N[10000]+()=10000+N[10000]+()=10000+N[10000]+()=1000
0+)=10000+
```
As you’ve noticed, it is calculated as neutral while it should not be. It is because it could not find
the score of “‫ترین‬ ‫سخت‬” in the sentiment dictionary. 
By extending sentiment dictionary to cover more objective words, this kind of errors will be solved.
In the next sentence, a misspelled word caused some wrong POS tagging. Also hopefully for this
statement it was not so objective, but using a proper spell checker tool, this kind of errors can be
reduced to minimum.
```
‫|زیرا‬N ‫|برنامھ‬N ‫|امتحانی‬ADJ ‫|ما‬PRO ‫|فشرده‬ADJ ‫|بود‬V_PA
```
Finally author ends comment by wishing future success for players.
Final score for this comment calculated as -2.
