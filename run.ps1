$computers = "strayhorn","hampton","horne","morton","basie"

rm -Force -Recurse C:\temp\bv011021\GARobocode\config | out-null
rm -Force -Recurse C:\temp\bv011021\GARobocode\lib\robocode\config | out-null
rm -Force -Recurse C:\temp\bv011021\GARobocode\workers | out-null

cd C:\temp\bv011021\GARobocode
        
Copy-Item -Path "./lib/" -Destination "./workers/worker1/" -Recurse -force | out-null
Copy-Item -Path "battleRunner.jar" -Destination "./workers/worker1/battleRunner.jar" -Recurse -force | out-null

Copy-Item -Path "./lib/" -Destination "./workers/worker2/" -Recurse -force | out-null
Copy-Item -Path "battleRunner.jar" -Destination "./workers/worker2/battleRunner.jar" -Recurse -force | out-null

Copy-Item -Path "./lib/" -Destination "./workers/worker3/" -Recurse -force | out-null
Copy-Item -Path "battleRunner.jar" -Destination "./workers/worker3/battleRunner.jar" -Recurse -force | out-null

Copy-Item -Path "./lib/" -Destination "./workers/worker4/" -Recurse -force | out-null
Copy-Item -Path "battleRunner.jar" -Destination "./workers/worker4/battleRunner.jar" -Recurse -force | out-null

foreach ($computer in $computers) {

    ROBOCOPY C:\temp\bv011021\GARobocode\workers \\$computer\c$\temp\bv011021\GARobocode\workers /MIR /xc /xn /xo /NFL /NDL /NJH /NJS /nc /ns | out-null

    Invoke-Command -Computername $computer -ScriptBlock { 

        ([WMICLASS]"\\localhost\ROOT\CIMV2:win32_process").Create("java -jar C:\temp\bv011021\GARobocode\workers\worker1\battleRunner.jar 15001", "C:\temp\bv011021\GARobocode\workers\worker1") | out-null
        ([WMICLASS]"\\localhost\ROOT\CIMV2:win32_process").Create("java -jar C:\temp\bv011021\GARobocode\workers\worker2\battleRunner.jar 15002", "C:\temp\bv011021\GARobocode\workers\worker2") | out-null
        ([WMICLASS]"\\localhost\ROOT\CIMV2:win32_process").Create("java -jar C:\temp\bv011021\GARobocode\workers\worker3\battleRunner.jar 15003", "C:\temp\bv011021\GARobocode\workers\worker3") | out-null
        ([WMICLASS]"\\localhost\ROOT\CIMV2:win32_process").Create("java -jar C:\temp\bv011021\GARobocode\workers\worker4\battleRunner.jar 15004", "C:\temp\bv011021\GARobocode\workers\worker4") | out-null

        get-process java
    }
}