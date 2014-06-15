#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>

typedef int64_t slot_t;

#define SLOT_SIZE 8

#define NULL_TAG -1
#define VOID_TAG 0
#define BOOL_TAG 1
#define CHAR_TAG 2
#define INT_TAG 3
#define REAL_TAG 4
#define STRING_TAG 5
#define RECORD_TAG 6
#define LIST_TAG 7

/**
 * Runtime support for While on X86.  Implemented in C for simplicity.
 */

int widthof(slot_t *type) {
 slot_t tag = *type;

  switch(tag) {
  case VOID_TAG:
    // void
    break;
  case NULL_TAG:
  case BOOL_TAG:
  case CHAR_TAG: 
  case INT_TAG: 
  case REAL_TAG: 
  case STRING_TAG: 
  case LIST_TAG:
    // bool
    return SLOT_SIZE;    
  case RECORD_TAG: 
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

  return 0;
}

void internal_tostring(slot_t *item, slot_t *type, char* buf) {

  // NOTE: this function is not working properly yet.

  slot_t tag = *type;

  switch(tag) {
  case VOID_TAG:
    // void
    break;
  case NULL_TAG:
      sprintf(buf,"null");
      break;
  case BOOL_TAG:
    // bool
    if(*item == 0) {
      sprintf(buf,"false");
    } else {
      sprintf(buf,"true");
    }
    break;
  case CHAR_TAG: 
    {
      // char
      char tmp[2];
      tmp[0] = *item;
      tmp[1] = '\0';
      sprintf(buf,"%s",tmp);
      break;
    }
  case INT_TAG:
    // int
    sprintf(buf,"%d",*item);
    break;
  case REAL_TAG:
    // real
    sprintf(buf,"%f",*item);
    break;
  case STRING_TAG:
    // string
    sprintf(buf,"%s",item);
    break;
  case RECORD_TAG:
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
    break;
    case LIST_TAG:
        ;
        
    slot_t size = *item;
    slot_t *etype = (slot_t *) *(item + 1);

    strcat(buf, "[");

    int i = 0;
    for (i = 0; i < size; i++) {
        if (i != 0) {
            strcat(buf, ", ");
        }

        char buf2[1024];
        slot_t *element = item + i + 2;
        internal_tostring(element, etype, buf2);
        strcat(buf, buf2);
    }

    strcat(buf, "]");

    break;
  }
}

void internal_print(slot_t *item, slot_t *type) {

  // NOTE: this function is not working properly yet.

  slot_t tag = *type;

  switch(tag) {
  case VOID_TAG:
    // void
    break;
  case NULL_TAG:
    printf("null");
    break;
  case BOOL_TAG:
    // bool
    if(*item == 0) {
      printf("false");
    } else {
      printf("true");
    }
    break;
  case CHAR_TAG: 
    {
      // char
      char tmp[2];
      tmp[0] = *item;
      tmp[1] = '\0';
      printf("%s",tmp);
      break;
    }
  case INT_TAG:
    // int
    printf("%d",*item);
    break;
  case REAL_TAG:
    // real
    printf("%f",*item);
    break;
  case STRING_TAG:
    // string
    printf("%s",item);
    break;
  case RECORD_TAG: 
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
    break;
  case LIST_TAG:
    ;

    slot_t size = *item;
    slot_t *etype = (slot_t *) *(item + 1);

    printf("[");

    int i = 0;
    for (i = 0; i < size; i++) {
        if (i != 0) {
            printf(", ");
        }

        slot_t *element = item + i + 2;
        internal_print(element, etype);
    }

    printf("]");
    break;
  }
}

void print(slot_t item, slot_t *type) {
  slot_t tag = *type;

  switch(tag) {
  case -1:
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
  strcpy(buf,lhs);

  switch(tag) {
  case -1:
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
  slot_t tag = *type;
  char buf[1024];

  switch(tag) {
  case -1:
  case 0:
  case 1:
  case 2:
  case 3:
  case 4:
    internal_tostring(&lhs,type,buf);
    break;
  default:
    internal_tostring((slot_t*)lhs,type,buf);
  }
  strcat(buf,rhs);
  char *result = malloc(1 + strlen(buf));
  strcpy(result, buf);
  return result;
}

slot_t lengthof(slot_t *item, slot_t *type) {
    slot_t tag = *type;

    switch (tag) {
        case STRING_TAG:
            return strlen((const char *) item);
        case LIST_TAG:
            return *item;
    }

    return 0;
}

slot_t indexof(slot_t *source, slot_t index, slot_t *type) {
    slot_t tag = *type;

    switch (tag) {
        case STRING_TAG:
            return ((char *) source)[index];
        case LIST_TAG:
            return *(source + index + 2);
    }

    return 0;
}

void str_indexof_assign(slot_t value, slot_t *source, slot_t index) {
    *(((char *) source) + index) = value;
}

slot_t *list_init(slot_t len, slot_t type) {
    slot_t * list = malloc((len + 2) * 8);
    slot_t * ltype = malloc(8);

    *ltype = *(slot_t *) type;

    *list = len;
    *(list + 1) = (slot_t) ltype;

    return list;
}

void list_indexof_assign(slot_t value, slot_t *source, slot_t index) {
    *(source + index + 2) = value;
}

slot_t *dup(slot_t *value, slot_t *type) {
    slot_t tag = *type;

    switch (tag) {
        case STRING_TAG:
            ;
            char *result = malloc(1 + strlen((char *) value));
            return (slot_t *) strcpy(result, (char *) value);
        case LIST_TAG:
            ;
            slot_t size = value[0];
            slot_t innerType = (slot_t) value[1];
            slot_t *list = list_init(size, innerType);

            int i = 0;
            for (i = 0; i < size; i++) {
                list_indexof_assign(value[i + 2], list, i);
            }
            return list;
    }

    return value;
}

slot_t *list_append(slot_t *lhs, slot_t *rhs) {
    slot_t size = lhs[0] + rhs[0];
    slot_t innerType = (slot_t) lhs[1];
    slot_t *list = list_init(size, innerType);

    int i = 0;
    for (i = 0; i < lhs[0]; i++) {
        list_indexof_assign(lhs[i + 2], list, i);
    }
    for (i = 0; i < rhs[0]; i++) {
        list_indexof_assign(rhs[i + 2], list, i + lhs[0]);
    }
    return list;
}

void printd(double item) {
    printf("%f\n", item);
}

double divd(double first, double second) {
    return first / second;
}

double cast(slot_t i) {
    return (double) i;
}

