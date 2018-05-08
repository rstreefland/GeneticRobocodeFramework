$computers = "strayhorn","hampton","horne","morton","basie"

foreach ($computer in $computers) {
    
    Invoke-Command -Computername $computer -ScriptBlock { 

        get-process *java* | kill -Force
        rm -Force -Recurse C:\temp\bv011021\GA*
    }
}