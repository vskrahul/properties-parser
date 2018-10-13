# Key Value Property parser

Parsing property file which has reference to other property.

This is a simple program which is covering basic following use cases:-

E.g.;

### Before parsing
 
1.a=1 
2.b=${a}+2 
3.c=${d} 
4.env=dev 
5.conection.dev=100 
6.max.connection=${connection.${env}}*1/2 

### After parsing it will be like... 
1.a=1
2.b=1+2 
3.c=${d} 
4.env=dev 
5.conection.dev=100 
6.max.connection=100*1/2 

## Note: It has been assumed that there are no circular referencing properties.