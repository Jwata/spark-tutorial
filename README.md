# Description
Apache spark Hands-on Exercise [Movie Reccomendation with MLib] (https://databricks-training.s3.amazonaws.com/movie-recommendation-with-mllib.html)

# Setup (Mac OS X)
```
brew install sbt apache-spark
```

# Steps
## Download Dataset  
* Go to [Grouplens download page] (http://grouplens.org/datasets/movielens/).  
* Download `ml-100k.zip` file
* Unarchieve it somewhere

## Run
```
sbt package
spark-submit ./target/scala-2.10/simple-spark-project_2.10-1.0.jar "path/to/dataset_dir"

# ... 
# (numRatings,100000)
# (numUsers,943)
# (numMovies,1682)
#  ...
```