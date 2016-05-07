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
RMSE (validation) = 0.8559153029074972 for the model trained with rank = 8, and numIter = 10, and lambda = 0.001.
RMSE (validation) = 0.8353052514614744 for the model trained with rank = 8, and numIter = 10, and lambda = 0.01.
RMSE (validation) = 0.8193030452779144 for the model trained with rank = 8, and numIter = 10, and lambda = 0.1.
RMSE (validation) = 0.8620679536862011 for the model trained with rank = 8, and numIter = 20, and lambda = 0.001.
RMSE (validation) = 0.8331036868588638 for the model trained with rank = 8, and numIter = 20, and lambda = 0.01.
RMSE (validation) = 0.819655003362246 for the model trained with rank = 8, and numIter = 20, and lambda = 0.1.
RMSE (validation) = 0.893704833700789 for the model trained with rank = 12, and numIter = 10, and lambda = 0.001.
RMSE (validation) = 0.8506822250518462 for the model trained with rank = 12, and numIter = 10, and lambda = 0.01.
RMSE (validation) = 0.8181397045444786 for the model trained with rank = 12, and numIter = 10, and lambda = 0.1.
RMSE (validation) = 0.9037592919084426 for the model trained with rank = 12, and numIter = 20, and lambda = 0.001.
RMSE (validation) = 0.8478152395194659 for the model trained with rank = 12, and numIter = 20, and lambda = 0.01.
RMSE (validation) = 0.8157949105223794 for the model trained with rank = 12, and numIter = 20, and lambda = 0.1.
The best model was trained with rank = 12, and numIter = 20, and lambda = 0.1, and its RMSE on the test set is 0.8158730470493469.
The baseline RMSE is 1.059782920707725
The best model improves the baseline by 23.02%.
(HellBent (2004),6.895058776472448)
(Purple Butterfly (Zi hudie) (2003),6.838192474427922)
(Detroit 9000 (1973),6.0853373192188736)
(D.A.N.G.A.N. Runner (Dangan ranna) (1996),5.89304400552993)
(Teddy Bear (Mis) (1981),5.8743734199767825)
(Jar, The (Khomreh) (1992),5.872059805903278)
(Sexual Life of the Belgians, The (La Vie sexuelle des Belges 1950-1978) (1994),5.772352576209522)
(Aerial, The (La Antena) (2007),5.77159162515898)
(Funeral in Berlin (1966),5.647809712307461)
(Rude (1995),5.641557723294751)
...
```
