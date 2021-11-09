<?php

$n1 = 1;
$n2 = 1;
$n3 = 1;

//TODO: Check for passed vatiables and method type (POST, GET)
if (count($_GET) > 0) {
    if (!isset($_GET["num1"]) ||
        !isset($_GET["num2"]) ||
        !isset($_GET["num3"])) {
        echo "All numbers are not set! Reload the page with that data.<br>";
        die();
    } else {
        echo "Numbers are valid, processing...<br>";
        $n1 = intval($_GET["num1"]);
        $n2 = intval($_GET["num2"]);
        $n3 = intval($_GET["num3"]);
    }
}

if (count($_POST) > 0) {
    if (!isset($_POST["num1"]) ||
        !isset($_POST["num2"]) ||
        !isset($_POST["num3"])) {
        echo "All numbers are not set! Reload the page with that data.<br>";
        die();
    } else {
        echo "Numbers are valid, processing...<br>";
        $n1 = intval($_POST["num1"]);
        $n2 = intval($_POST["num2"]);
        $n3 = intval($_POST["num3"]);
    }
}

$sum = $n1 + $n2 + $n3;
$sub = $n1 - $n2 - $n3;
$mul = $n1 * $n2 * $n3;
$div = $n1 / $n2 / $n3;

echo sprintf("Sum: %d, Subtraction: %d, Multiplication: %d, Division: %d", $sum, $sub, $mul, $div);

?>