
# ExpBlock
This is the artifact for the manuscript:

"A Randomized Blocking Structure for Streaming Record Linkage", 

co-authored by D. Karapiperis (IHU), C. Tjortjis (IHU), and V. Verykios (HOU) accepted at PVLDB 2023 (Vancouver, Canada).


## Abstract

A huge amount of data, in terms of streams, is collected nowadays via a variety of sources, such as sensors, mobile devices, or even raw log files. The unprecedented rate at which these data are generated and collected calls for novel record linkage methods to identify matching records pairs, which refer to the same real-world entity. Blocking partitions data into blocks, whose records share a blocking key, to reduce the number of candidate record pairs. 


This paper introduces ExpBlock, a randomized record linkage structure, which guarantees that both the most frequently accessed and recently used blocks remain in main memory and, additionally, the records within a block are renewed on a rolling basis. 


Specifically, the probability of inactive blocks and older records to remain in main memory decays in order to give space to more promising blocks and fresher records, respectively. We implement these features using random choices instead of utilizing cumbersome ranking data structures in order to favour simplicity of implementation and efficiency. 


We showcase, through the experimental evaluation, that ExplBlock scales efficiently to data streams by providing accurate results in a timely fashion.

## Dependencies
- Java Runtime Environment 8 (and onwards)
- Maven 3
- All component dependencies are managed by Maven

## Running the artifact

- Clone the repo
- Build the jar with `mvn clean package`
- Download and place the data files [test_voters_A.txt](https://www.dropbox.com/s/lgzky6gpwz1vpi8/test_voters_A.txt?dl=0) and [test_voters_B.txt](https://www.dropbox.com/s/67zdw71hlju89ec/test_voters_B.txt?dl=0) into `<repo>\target` 
- From the command line, navigate into `<repo>\target` and run the project with `java -jar ExpBlock-1.0-SNAPSHOT-jar-with-dependencies.jar`
- The results of the linkage will be found in `<repo>\target\results.txt` (Write privileges are required in `<repo>\target`)
