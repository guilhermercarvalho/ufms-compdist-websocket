#include <stdio.h>


int main(int argc, char *argv[], char *envp[])
{
	int i = 0;
	
	while (envp[i])
		printf("<h1>%s</h1>\n", envp[i++]);
}
