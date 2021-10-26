**<H1> Coding Challenge </H1>**
<H3>Steps to execute the code</H3>
<H3>Pre-requisites</H3>
<ul>
<li>Java 8</li>
<li>Maven</li>
</ul>
<BR>
<ol>
<li>Clone this project using `git clone git@github.com:niteshnair/JavaCodingChallenge.git`</li>
<li>Make sure all the pre-requisite conditions has been satisfied </li>
<li>
Command Line arguments to be provided are as follows
<div>
    <ul>
        <li>First Argument  : Log File path* (Mandatory Argument)</li>
        <li>Second Argument : Db Path (Optional Argument)</li>
        <li>Third Argument  : Db Name (Optional Argument)</li>
    </ul>
</div>
</li>
<li>To execute the code provide the below command in a command line opened at the project directory</li>
`mvn exec:java -Dexec.mainClass=logreader.LogReader -Dexec.args="logfilepath dbpath dbname"`
</ol>