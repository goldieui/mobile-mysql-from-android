<?php
mysql_connect("localhost:3306","root","root");
mysql_select_db("android");

if (isset($_REQUEST['country'])) {
   $q = mysql_query("select ccode from countries where country = '".$_REQUEST['country']."'");
} else {
  $q=mysql_query("SELECT * FROM countries");
}
while($e=mysql_fetch_assoc($q))
        $output[]=$e;

print(json_encode($output));
 
mysql_close();
?>