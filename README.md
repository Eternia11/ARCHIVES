# ARCHIVES

v0.5 :
	- Allow to read a log file in .csv format, the log file must have 6 column representing for each line of a case : the caseID, the timestamp, the performative, the sender resource, the receiver resource, the activity performed
	- Allow to create graphs and export them to GraphML format files
	- Allow to create place/transition petri nets and export them to PNML format files (proM importation is the best way to read these files and visualize the net)
	- Allow to create the Petri Net representing the logs following the Alpha Algorithm
	- Allow to create workflows and export them to XPDL format files in order to visualize them with the "Together Workflow Editor" software
	- Read the toy_case.csv log file and build :
		- a hierarchical graph of its resources according the delegations recorded
		- an interaction graph according the informations exchanged
		- a petri net representing the log file according to the alpha algorithm
		- a draft of a workflow meant to represent the behavior of each resource, using the alpha algorithm to build it
