def generate_sql(email_id, video_id, base_url, frame_count, current_row):
    sql_statements = []
    for frame_index in range(1, frame_count + 1):
        frame_url = f"{base_url}v1e{frame_index}.jpg"
        sql_statement = f"INSERT INTO enhanced_frame (FRAME_ID, EMAIL_ID, VIDEO_ID, ENHANCED_FRAMES_URL) " \
                        f"VALUES ('{current_row + frame_index}', '{email_id}', '{video_id}', '{frame_url}');"
        sql_statements.append(sql_statement)
    return sql_statements
