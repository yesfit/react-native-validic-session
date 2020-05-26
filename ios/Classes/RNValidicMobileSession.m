#import "RNValidicMobileSession.h"

@implementation RNValidicMobileSession{
    bool hasListeners;
}

RCT_EXPORT_MODULE()

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

-(NSDictionary *)constantsToExport {
    return @{
             @"SESSION_VERSION" : [VLDSession libraryVersion]
             };
}

RCT_EXPORT_METHOD(startSession:(NSString *)userID organizationID:(NSString *)orgID accessToken:(NSString *)token) {
    VLDUser *user = [[VLDUser alloc] initWithValidicUserID:userID organizationID:orgID accessToken:token];
    [[VLDSession sharedInstance] startSessionWithUser:user];
}

RCT_EXPORT_METHOD(endSession) {
    [[VLDSession sharedInstance] endSession];
}

RCT_EXPORT_METHOD(submitRecord:(VLDRecord *)record) {
    [[VLDSession sharedInstance] submitRecord:record];
}

RCT_EXPORT_METHOD(getUser:(RCTResponseSenderBlock)callback) {
    VLDUser *user = [[VLDSession sharedInstance] user];
    
    if (user != nil) {
        callback( @[@{ @"user" : @{ @"user_id" : user.validicUserID,
                                    @"org_id" : user.organizationID,
                                    @"user_token" : user.accessToken
                                    }}]);
    } else {
        callback( @[@{ @"user" : [NSNull null] }]);
    }
    
}

- (NSArray<NSString *> *)supportedEvents {
    return @[@"validic:session:onsuccess", @"validic:session:onerror"];
}

- (void)startObserving {
    hasListeners = YES;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(didSubmitRecord:) name:kVLDRecordSubmittedNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(failedToSubmitRecord:) name:kVLDRecordSubmissionFailedNotification object:nil];
}

- (void)stopObserving {
    hasListeners = NO;
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kVLDRecordSubmittedNotification object:nil];
    [[NSNotificationCenter defaultCenter] removeObserver:self name:kVLDRecordSubmissionFailedNotification object:nil];
}

- (void)didSubmitRecord:(NSNotification *)notification {
    VLDRecord *record = (VLDRecord *)notification.object;
    NSDictionary *recordDic = [record JSONObject];
    if(hasListeners){
        [self sendEventWithName:@"validic:session:onsuccess" body:recordDic];
    }
}

- (void)failedToSubmitRecord:(NSNotification *)notification {
    NSError *error = notification.userInfo[@"error"];
    VLDRecord *record = (VLDRecord *)notification.object;
    NSDictionary *dictionary = @{@"record": [record JSONObject], @"error": [error userInfo]};
    if(hasListeners){
        [self sendEventWithName:@"validic:session:onsuccess" body:dictionary];
    }
}

+(BOOL) requiresMainQueueSetup{
    return YES;
}


@end
