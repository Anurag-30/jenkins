# jenkins

## OBSERVATIONS


1. Blueocean plugin looks for a file with the name "Jenkinsfile" to create a pipeline 
2. If the pipeline is manaully created from the UI it creates the jenkinsfile(code) in the repo with the exact code
3. Don't make the changes to the jenkinsfile that is created by the pipeline in UI, it corrupts.
4. The pipeline is specific to the repo used to create it. All the contents of the repo are available. ---> Doesn't require a git clone


## NEED TO

1. Do we need to trigger the pipeline for every commit?
2. Trigerring build from blueocean UI. ---> Done

## Missing Features

Grouping the jobs in blueocean under a folder.
