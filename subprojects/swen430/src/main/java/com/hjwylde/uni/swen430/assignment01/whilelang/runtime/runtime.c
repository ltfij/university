#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

typedef int64_t slot_t;

#define SLOT_SIZE 8

/**
 * Runtime support for While on X86.  Implemented in C for simplicity.
 */

int widthof(slot_t *type) {
 slot_t tag = *type;

  switch(tag) {
  case 0:
    // void
    break;
  case 1:
  case 2: 
  case 3: 
  case 4: 
  case 5: 
    // bool
    return SLOT_SIZE;    
  case 6: 
    {
      int i;
      int width = 0;
      // record
      slot_t nfields = *(++type);
      for(i=0;i!=nfields;++i) {
	slot_t fieldNameSize = *(++type);
	type = ((void *)type) + fieldNameSize + 1;
	width = width + widthof(type);
	// FIXME: this is clearly broken here, since we don't properly increment type.  Rather, we're assuming it's a single slot.
      }
      return width;
    }
  }
}

void internal_tostring(slot_t *item, slot_t *type, char* buf) {

  // NOTE: this function is not working properly yet.

  slot_t tag = *type;

  switch(tag) {
  case 0:
    // void
    break;
  case 1:
    // bool
    if(*item == 0) {
      sprintf(buf,"false");
    } else {
      sprintf(buf,"true");
    }
    break;
  case 2: 
    {
      // char
      char tmp[2];
      tmp[0] = *item;
      tmp[1] = '\0';
      sprintf(buf,"%s",tmp);
      break;
    }
  case 3:
    // int
    sprintf(buf,"%d",*item);
    break;
  case 4:
    // real
    sprintf(buf,"%g",*item);
    break;
  case 5:
    // string
    sprintf(buf,"%s",item);
    break;
  case 6: 
    {
      int i;
      // record
      sprintf(buf,"{");
      buf += strlen(buf);
      slot_t nfields = *(++type);
      for(i=0;i!=nfields;++i) {
	if(i != 0) {
	  sprintf(buf,",");
	  buf += strlen(buf);
	}
	slot_t fieldNameSize = *(++type);
	sprintf(buf,"%s:",++type);
	buf += strlen(buf);
	type = ((void *)type) + fieldNameSize + 1;
	internal_tostring(item,type,buf);
	buf += strlen(buf);
	item = ((void *)item) + widthof(type);
	// FIXME: this is clearly broken here, because we don't increment type correctly.
      }
      sprintf(buf,"}");
    }
  }
}

void internal_print(slot_t *item, slot_t *type) {

  // NOTE: this function is not working properly yet.

  slot_t tag = *type;

  switch(tag) {
  case 0:
    // void
    break;
  case 1:
    // bool
    if(*item == 0) {
      printf("false");
    } else {
      printf("true");
    }
    break;
  case 2: 
    {
      // char
      char tmp[2];
      tmp[0] = *item;
      tmp[1] = '\0';
      printf("%s",tmp);
      break;
    }
  case 3:
    // int
    printf("%d",*item);
    break;
  case 4:
    // real
    printf("%g",*item);
    break;
  case 5:
    // string
    printf("%s",item);
    break;
  case 6: 
    {
      int i;
      // record
      printf("{");
      slot_t nfields = *(++type);
      for(i=0;i!=nfields;++i) {
	if(i != 0) {
	  printf(",");
	}
	slot_t fieldNameSize = *(++type);
	printf("%s:",++type);
	type = ((void *)type) + fieldNameSize + 1;
	internal_print(item,type);
	item = ((void *)item) + widthof(type);
	// FIXME: this is clearly broken here, because we don't increment type correctly.
      }
      printf("}");
    }
  }
}

void print(slot_t item, slot_t *type) {
  slot_t tag = *type;
  switch(tag) {
  case 0:
  case 1:
  case 2:
  case 3:
  case 4:
    internal_print(&item,type);    
    break;
  default:
    internal_print((slot_t*)item,type); 
  }
  printf("\n");
}

char *str_append(char *lhs, char *rhs) {
  char *result = malloc(1 + strlen(lhs) + strlen(rhs));
  strcpy(result,lhs);
  return strcat(lhs,rhs);
}

char *str_left_append(char *lhs, slot_t rhs, slot_t *type) {
  int lhsLen = strlen(lhs);
  slot_t tag = *type;
  char buf[1024];
  strcat(buf,lhs);

  switch(tag) {
  case 0:
  case 1:
  case 2:
  case 3:
  case 4:
    internal_tostring(&rhs,type,buf + lhsLen);
    break;
  default:
    internal_tostring((slot_t*)rhs,type,buf + lhsLen);
  }
  char *result = malloc(1 + strlen(buf));
  strcpy(result, buf);
  return result;
}

char *str_right_append(slot_t lhs, char *rhs, slot_t *type) {
  return "blah blah";
}



