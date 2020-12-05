#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define NUM_OPTIONS 3
char* OPTIONS[NUM_OPTIONS] = {"ROCK", "PAPER", "SCISSORS"};

void trim(char*);
void uppercase(char*);
int my_strcmp(char*, char*);
char streq(char*,char*);
int get_choice_number(char*);

int main()
{
	char play_again;
	printf("Let's play Rock Paper Scissors!\n");
	while(1)
	{
		printf("\n\n");
		printf("------------------------------------------\n");
		printf("Please Enter Your Choice:\n");
		char buf[100];
		//fgets();
		trim(buf);
		uppercase(buf);

		int choice_number = get_choice_number(buf);
		if(choice_number == -1)
		{
			printf("bad input\n");
			continue;
		}

		srand(time(NULL));        
		int computer_choice = rand() % 3;

		printf("You: %s\n", OPTIONS[choice_number]);
		printf("CPU: %s\n", OPTIONS[computer_choice]);

		if(choice_number == computer_choice)
		{
			printf("It's a Tie!\n\n");
		}
		else if((computer_choice-1)%3 == choice_number)
		{
			printf("You lose!\n\n");
		}
		else
		{
			printf("You Win!\n");
		}

		printf("Play Again?\n");
		//fgets();
		//sscanf();
		if(play_again == 'n')
		{
			break;
		}
    }


}

void trim(char* str)
{

}

void uppercase(char* str)
{

}

int my_strcmp(char* a, char* b)
{
    //optional: Implement your own strcmp if you want
    //try to write it in two lines for for challenge

}

char streq(char *a, char *b)
{
	
}

int get_choice_number(char* str)
{


}