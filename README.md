Steps to follow In PostgreSQL database
1. Create table
   
   CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    user_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (id)
)


2. Create Trigger
   
   CREATE TRIGGER notification_added_in_queue
    AFTER INSERT OR UPDATE 
    ON public.users
    FOR EACH ROW
    EXECUTE FUNCTION public.notify_new_notification_in_queue();

3. Create trigger function
   
   CREATE OR REPLACE FUNCTION public.notify_new_notification_in_queue()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
  IF TG_OP = 'INSERT' THEN
    PERFORM pg_notify('notifications', NEW.id::text);
  ELSIF TG_OP = 'UPDATE' THEN
    PERFORM pg_notify('notifications', NEW.id::text);
END IF;
RETURN NEW;
END;
$BODY$;
