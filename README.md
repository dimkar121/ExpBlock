
This is the artifact for the submission 

"A Randomized Blocking Structure for Streaming Record Linkage", co-authored by D. Karapiperis (IHU), C. Tjortjis (IHU), and V. Verykios (HOU)


Abstract

A huge amount of data, in terms of streams, is collected nowadays via a variety of sources, such as sensors, mobile devices, or even raw
log files. The unprecedented rate at which these data are generated and collected calls for novel record linkage methods to identify
matching records pairs, which refer to the same real-world entity. 

Blocking partitions data into blocks, whose records share a blocking key, to reduce the number of candidate record pairs. This paper
introduces ExpBlock, a randomized streaming record linkage structure, which guarantees that both the most frequently accessed and
recently used blocks remain in main memory and, additionally, the records within a block are renewed on a rolling basis. 

Specifically, the probability of inactive blocks and older records to remain in main memory exponentially decays in order to give space to fresher
blocks and records, respectively.We implement these features using random choices instead of utilizing cumbersome data structures in order to favour simplicity of implementation and efficiency. 


Execution

Download the executable jar `ExpBlock.jar`

Then, download and place the data file [ncvoters_A.txt](https://www.dropbox.com/s/5a48pnqbdqcd6w4/ncvoters_A.txt?dl=0) in the same directory with `ExpBlock.jar`

In the command line, run `ExpBlock.jar` using `java -jar ExpBlock.jar`
