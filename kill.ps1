$computers = "strayhorn","hampton","horne","morton","basie"

foreach ($computer in $computers) {
    
    Invoke-Command -Computername $computer -ScriptBlock { 

        get-process *java* | kill -Force
    }
}