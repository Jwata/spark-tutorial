# Description
Apache spark Hands-on Exercise [Movie Reccomendation with MLib] (https://databricks-training.s3.amazonaws.com/movie-recommendation-with-mllib.html)

# Setup (Mac OS X)
```
brew install sbt apache-spark
```

# Steps
## Download Dataset  
* Go to [Grouplens download page] (http://grouplens.org/datasets/movielens/).  
* Download `ml-1m.zip` file
* Unarchieve it somewhere

## Run
```
sbt assembly
spark-submit ./build/movie-lens-als-assembly-1.0.jar "path/to/dataset_dir"

```
```
# output
...
RMSE (validation) = 0.922607869933657 for the model trained with rank = 8, and numIter = 10.
RMSE (validation) = 0.9175301036925269 for the model trained with rank = 8, and numIter = 20.
RMSE (validation) = 0.9478070305533259 for the model trained with rank = 12, and numIter = 10.
RMSE (validation) = 0.9537503800594634 for the model trained with rank = 12, and numIter = 20.
The best model was trained with rank = 8, and numIter = 20, and its RMSE on the test set is 0.9176744991075861.
The baseline RMSE is 1.1135195727043978
The best model improves the baseline by 17.59%.
(Careful (1992),9.350469174334245)
(Julien Donkey-Boy (1999),9.080953496873505)
(My Life So Far (1999),8.994386012809013)
(Mighty Peking Man (Hsing hsing wang) (1977),8.665958611190685)
(Tango Lesson, The (1997),8.557742341101669)
(Visitors, The (Les Visiteurs) (1993),8.541216829747123)
(Actor's Revenge, An (Yukinojo Henge) (1963),8.375598880122931)
(Roadside Prophets (1992),8.09882035972996)
(Melody Time (1948),7.747012578871741)
(Stars and Bars (1988),7.6931738098318325)
...
```
